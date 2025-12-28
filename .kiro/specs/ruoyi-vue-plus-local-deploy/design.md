# Design Document

## Overview

本设计文档描述 RuoYi-Vue-Plus 项目本地开发环境的部署架构。采用混合部署模式：基础设施（MySQL、Redis、MinIO）通过 Docker Compose 容器化部署，后端和前端以源码方式运行，实现开发便捷性与环境一致性的平衡。

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        本地开发环境                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────┐     ┌─────────────────┐                   │
│  │   Frontend      │     │   Backend       │                   │
│  │   (源码运行)     │────▶│   (源码运行)     │                   │
│  │   Port: 80      │     │   Port: 8080    │                   │
│  │   Vue3 + Vite   │     │   Spring Boot   │                   │
│  └─────────────────┘     └────────┬────────┘                   │
│                                   │                             │
│  ┌────────────────────────────────┼────────────────────────────┐│
│  │              Docker Compose    │                            ││
│  │  ┌──────────┐  ┌──────────┐  ┌┴─────────┐                  ││
│  │  │  MySQL   │  │  Redis   │  │  MinIO   │                  ││
│  │  │  :3306   │  │  :6379   │  │  :9000   │                  ││
│  │  └──────────┘  └──────────┘  └──────────┘                  ││
│  └─────────────────────────────────────────────────────────────┘│
│                                                                 │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │                    数据持久化目录                             ││
│  │  ./docker/mysql/data  ./docker/redis/data  ./docker/minio  ││
│  └─────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────┘
```

## Components and Interfaces

### 1. Docker Compose 基础设施层

负责管理所有容器化服务的生命周期。

```yaml
# docker-compose.yml 核心结构
services:
  mysql:      # MySQL 8.0 数据库
  redis:      # Redis 7.x 缓存
  minio:      # MinIO 对象存储
```

**接口定义：**
- MySQL: `localhost:3306`, 用户: `root`, 密码: `root`, 数据库: `ry-vue`
- Redis: `localhost:6379`, 密码: `ruoyi123`
- MinIO: API `localhost:9000`, Console `localhost:9001`, 用户: `ruoyi`, 密码: `ruoyi123`

### 2. 数据库初始化组件

通过 Docker 挂载 SQL 脚本实现自动初始化。

```
./docker/mysql/init/
├── 01_ry_vue_5.X.sql      # 系统核心表
├── 02_ry_job.sql          # 定时任务表
└── 03_ry_workflow.sql     # 工作流表
```

**初始化流程：**
1. MySQL 容器首次启动检测 `/docker-entrypoint-initdb.d/` 目录
2. 按文件名顺序执行 SQL 脚本
3. 完成后标记初始化完成，后续启动跳过

### 3. 后端服务组件

Spring Boot 应用，以源码方式运行。

**启动命令：**
```bash
# 开发模式启动
mvn spring-boot:run -pl ruoyi-admin

# 或构建后启动
mvn clean package -DskipTests
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

**配置文件：** `ruoyi-admin/src/main/resources/application-local.yml`

### 4. 前端应用组件

Vue3 + Vite 应用，以源码方式运行。

**启动命令：**
```bash
cd plus-ui
npm install
npm run dev
```

**代理配置：** `vite.config.ts` 中配置 API 代理到后端 8080 端口

### 5. 构建脚本组件

提供一键操作脚本简化开发流程。

```
./scripts/
├── start-infra.bat/.sh      # 启动基础设施
├── stop-infra.bat/.sh       # 停止基础设施
├── rebuild-backend.bat/.sh  # 重构建后端
└── init-all.bat/.sh         # 初始化全部环境
```

## Data Models

### 配置数据模型

```yaml
# 环境配置 (.env)
MYSQL_ROOT_PASSWORD: root
MYSQL_DATABASE: ry-vue
REDIS_PASSWORD: ruoyi123
MINIO_ROOT_USER: ruoyi
MINIO_ROOT_PASSWORD: ruoyi123
```

### 目录结构模型

```
project-root/
├── docker/                    # Docker 相关文件
│   ├── docker-compose.yml     # 容器编排文件
│   ├── mysql/
│   │   ├── init/              # SQL 初始化脚本
│   │   ├── data/              # 数据持久化
│   │   └── conf/              # MySQL 配置
│   ├── redis/
│   │   ├── conf/              # Redis 配置
│   │   └── data/              # 数据持久化
│   └── minio/
│       └── data/              # 数据持久化
├── scripts/                   # 操作脚本
├── ruoyi-admin/               # 后端源码
└── plus-ui/                   # 前端源码
```


## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system-essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

由于本项目是部署配置类任务，主要涉及文件创建、服务启动和连接验证，大部分验收标准适合通过示例测试（example tests）而非属性测试（property tests）来验证。以下是可测试的关键验证点：

### Example Tests (示例测试)

1. **基础设施启动验证**
   - 验证 MySQL 容器启动后 3306 端口可连接
   - 验证 Redis 容器启动后 6379 端口可连接
   - 验证 MinIO 容器启动后 9000/9001 端口可连接
   - **Validates: Requirements 1.1, 1.2, 1.3**

2. **数据库初始化验证**
   - 验证 ry-vue 数据库存在
   - 验证系统核心表（如 sys_user, sys_role）存在
   - 验证定时任务表（如 sj_job）存在
   - 验证工作流表存在
   - **Validates: Requirements 2.1, 2.2, 2.3, 2.4**

3. **后端服务验证**
   - 验证 JAR 文件构建成功
   - 验证后端 8080 端口可访问
   - 验证健康检查接口返回正常
   - **Validates: Requirements 3.1, 3.5**

4. **前端服务验证**
   - 验证 node_modules 目录存在
   - 验证前端开发服务器启动
   - 验证 API 代理配置正确
   - **Validates: Requirements 4.1, 4.2, 4.5**

5. **配置文件验证**
   - 验证 docker-compose.yml 文件存在且语法正确
   - 验证 application-local.yml 包含正确的连接配置
   - 验证脚本文件存在且可执行
   - **Validates: Requirements 6.1, 6.2, 6.3, 6.5**

## Error Handling

### Docker 容器错误处理

| 错误场景 | 处理方式 |
|---------|---------|
| 端口被占用 | 输出端口冲突信息，建议修改端口映射 |
| 镜像拉取失败 | 重试拉取或提示配置镜像加速器 |
| 容器启动失败 | 保留容器日志，输出错误信息 |
| 数据目录权限不足 | 提示修改目录权限 |

### 数据库初始化错误处理

| 错误场景 | 处理方式 |
|---------|---------|
| SQL 语法错误 | 记录错误日志，跳过错误脚本继续执行 |
| 表已存在 | 使用 IF NOT EXISTS 避免重复创建 |
| 连接超时 | 增加重试机制，等待数据库就绪 |

### 后端服务错误处理

| 错误场景 | 处理方式 |
|---------|---------|
| 数据库连接失败 | 输出连接信息，提示检查 Docker 服务状态 |
| Redis 连接失败 | 输出连接信息，提示检查 Redis 服务状态 |
| 端口被占用 | 提示修改 server.port 配置 |

### 前端服务错误处理

| 错误场景 | 处理方式 |
|---------|---------|
| 依赖安装失败 | 提示清理 node_modules 重新安装 |
| 端口被占用 | 自动切换到其他可用端口 |
| API 代理失败 | 提示检查后端服务是否启动 |

## Testing Strategy

### 测试方法

由于本项目是部署配置类任务，采用以下测试策略：

1. **手动验证测试**
   - 执行部署脚本后手动验证各服务状态
   - 通过浏览器访问前端页面验证功能
   - 通过 API 工具测试后端接口

2. **自动化脚本验证**
   - 提供健康检查脚本验证所有服务状态
   - 脚本检查端口连通性
   - 脚本检查数据库表是否创建成功

### 验证脚本示例

```bash
# health-check.sh
#!/bin/bash

echo "检查 MySQL..."
nc -z localhost 3306 && echo "MySQL OK" || echo "MySQL FAILED"

echo "检查 Redis..."
nc -z localhost 6379 && echo "Redis OK" || echo "Redis FAILED"

echo "检查 MinIO..."
nc -z localhost 9000 && echo "MinIO OK" || echo "MinIO FAILED"

echo "检查后端服务..."
curl -s http://localhost:8080/actuator/health | grep -q "UP" && echo "Backend OK" || echo "Backend FAILED"
```

### 测试检查清单

- [ ] Docker Compose 启动无错误
- [ ] MySQL 可连接且数据库已初始化
- [ ] Redis 可连接
- [ ] MinIO 可连接
- [ ] 后端服务启动成功
- [ ] 前端开发服务器启动成功
- [ ] 前端可正常访问后端 API
- [ ] 登录功能正常（默认账号：admin/admin123）
