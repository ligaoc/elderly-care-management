# RuoYi-Vue-Plus 本地开发环境部署

本项目提供 RuoYi-Vue-Plus 的本地开发环境快速部署方案，采用 Docker 容器化基础设施 + 源码运行应用的混合模式。

## 前置条件

- Docker Desktop (Windows/Mac) 或 Docker Engine (Linux)
- JDK 17+
- Maven 3.8+
- Node.js 18+
- Git

## 快速开始

### 1. 一键初始化环境

```bash
# Windows
scripts\init-all.bat

# Linux/Mac
chmod +x scripts/*.sh
./scripts/init-all.sh
```

### 2. 克隆项目代码

```bash
# 后端项目
git clone https://gitee.com/dromara/RuoYi-Vue-Plus.git

# 前端项目
git clone https://gitee.com/JavaLionLi/plus-ui.git
```

### 3. 配置后端

将 `config/application-local.yml` 复制到后端项目：
```bash
cp config/application-local.yml RuoYi-Vue-Plus/ruoyi-admin/src/main/resources/
```

### 4. 启动服务

```bash
# 启动后端（在 RuoYi-Vue-Plus 目录下）
cd RuoYi-Vue-Plus
../scripts/start-backend.bat  # Windows
../scripts/start-backend.sh   # Linux/Mac

# 启动前端（在 plus-ui 目录下）
cd plus-ui
npm install
npm run dev
```

### 5. 访问系统

- 前端地址: http://localhost:80
- 后端 API: http://localhost:8080
- MinIO 控制台: http://localhost:9001

默认账号: `admin` / `admin123`

## 目录结构

```
.
├── docker/                    # Docker 配置
│   ├── docker-compose.yml     # 容器编排
│   ├── mysql/                 # MySQL 配置和数据
│   ├── redis/                 # Redis 配置和数据
│   └── minio/                 # MinIO 数据
├── config/                    # 应用配置文件
│   ├── application-local.yml  # 后端本地配置
│   └── frontend.env.development # 前端环境配置
├── scripts/                   # 操作脚本
│   ├── init-all.*            # 一键初始化
│   ├── start-infra.*         # 启动基础设施
│   ├── stop-infra.*          # 停止基础设施
│   ├── start-backend.*       # 启动后端
│   ├── start-frontend.*      # 启动前端
│   ├── rebuild-backend.*     # 重构建后端
│   ├── health-check.*        # 健康检查
│   └── download-sql.*        # 下载 SQL 脚本
└── README.md
```

## 服务端口

| 服务 | 端口 | 用户名 | 密码 |
|------|------|--------|------|
| MySQL | 3306 | root | root |
| Redis | 6379 | - | ruoyi123 |
| MinIO API | 9000 | ruoyi | ruoyi123 |
| MinIO Console | 9001 | ruoyi | ruoyi123 |
| 后端服务 | 8080 | - | - |
| 前端服务 | 80 | - | - |

## 常用命令

```bash
# 启动/停止基础设施
scripts/start-infra.bat   # 启动 MySQL、Redis、MinIO
scripts/stop-infra.bat    # 停止所有容器

# 重构建后端
scripts/rebuild-backend.bat

# 健康检查
scripts/health-check.bat

# 查看容器日志
cd docker && docker-compose logs -f mysql
cd docker && docker-compose logs -f redis
```

## 开发流程

1. **修改 Java 代码后**：运行 `scripts/rebuild-backend.bat` 重新构建
2. **修改前端代码后**：自动热更新，无需操作
3. **修改数据库**：直接连接 MySQL 执行 SQL

## 常见问题

### 端口被占用
修改 `docker/docker-compose.yml` 中的端口映射，同时更新 `config/application-local.yml` 中的连接配置。

### MySQL 初始化失败
1. 删除 `docker/mysql/data` 目录
2. 重新运行 `scripts/download-sql.bat` 下载 SQL 脚本
3. 重新启动 `scripts/start-infra.bat`

### 后端连接数据库失败
1. 确认 Docker 容器正在运行：`docker ps`
2. 运行健康检查：`scripts/health-check.bat`
3. 检查配置文件中的连接信息

## 参考链接

- [RuoYi-Vue-Plus 官方文档](https://plus-doc.dromara.org)
- [plus-ui 前端项目](https://gitee.com/JavaLionLi/plus-ui)
