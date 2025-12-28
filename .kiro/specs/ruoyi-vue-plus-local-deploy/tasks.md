# Implementation Plan: RuoYi-Vue-Plus 本地部署

## Overview

本实现计划将创建完整的本地开发环境部署方案，包括 Docker 基础设施配置、数据库初始化、后端和前端配置，以及便捷的操作脚本。

## Tasks

- [x] 1. 创建 Docker 基础设施配置
  - [x] 1.1 创建 docker 目录结构和 docker-compose.yml 文件
    - 创建 `docker/` 目录及子目录
    - 编写 docker-compose.yml 配置 MySQL、Redis、MinIO 服务
    - 配置数据持久化挂载目录
    - _Requirements: 1.1, 1.2, 1.3, 1.4_

  - [x] 1.2 创建 MySQL 配置文件
    - 创建 `docker/mysql/conf/my.cnf` 配置文件
    - 配置字符集、时区等参数
    - _Requirements: 1.1_

  - [x] 1.3 创建 Redis 配置文件
    - 创建 `docker/redis/conf/redis.conf` 配置文件
    - 配置密码、持久化策略
    - _Requirements: 1.2_

- [x] 2. 配置数据库自动初始化
  - [x] 2.1 下载并准备 SQL 初始化脚本
    - 从 RuoYi-Vue-Plus 仓库下载 SQL 脚本
    - 创建 `docker/mysql/init/` 目录
    - 按顺序命名脚本确保执行顺序
    - _Requirements: 2.1, 2.2, 2.3, 2.4_

  - [x] 2.2 配置 MySQL 容器自动执行初始化脚本
    - 在 docker-compose.yml 中配置 init 目录挂载
    - 确保脚本在容器首次启动时执行
    - _Requirements: 2.1, 2.2, 2.3, 2.4_

- [x] 3. Checkpoint - 验证基础设施
  - 启动 Docker 容器验证服务正常
  - 检查数据库初始化是否成功
  - 确保所有端口可访问

- [x] 4. 配置后端源码运行环境
  - [x] 4.1 创建本地开发配置文件
    - 创建 `application-local.yml` 配置文件
    - 配置数据库连接指向 Docker MySQL
    - 配置 Redis 连接指向 Docker Redis
    - 配置 MinIO 连接指向 Docker MinIO
    - _Requirements: 3.2, 3.3, 3.4, 6.1, 6.2, 6.3_

  - [x] 4.2 创建后端启动脚本
    - 创建 Windows 批处理脚本 `scripts/start-backend.bat`
    - 创建 Linux/Mac shell 脚本 `scripts/start-backend.sh`
    - 配置使用 local profile 启动
    - _Requirements: 3.1, 3.5_

- [x] 5. 配置前端源码运行环境
  - [x] 5.1 创建前端环境配置说明
    - 创建前端项目克隆和配置说明
    - 配置 API 代理指向后端 8080 端口
    - _Requirements: 4.1, 4.2, 4.3, 4.5_

  - [x] 5.2 创建前端启动脚本
    - 创建 Windows 批处理脚本 `scripts/start-frontend.bat`
    - 创建 Linux/Mac shell 脚本 `scripts/start-frontend.sh`
    - _Requirements: 4.2, 4.3_

- [x] 6. 创建便捷操作脚本
  - [x] 6.1 创建基础设施启动/停止脚本
    - 创建 `scripts/start-infra.bat` 和 `scripts/start-infra.sh`
    - 创建 `scripts/stop-infra.bat` 和 `scripts/stop-infra.sh`
    - _Requirements: 1.1, 1.2, 1.3_

  - [x] 6.2 创建后端重构建脚本
    - 创建 `scripts/rebuild-backend.bat` 和 `scripts/rebuild-backend.sh`
    - 包含 Maven 构建和重启命令
    - _Requirements: 5.1, 5.3_

  - [x] 6.3 创建一键初始化脚本
    - 创建 `scripts/init-all.bat` 和 `scripts/init-all.sh`
    - 包含完整的环境初始化流程
    - _Requirements: 5.3, 6.5_

  - [x] 6.4 创建健康检查脚本
    - 创建 `scripts/health-check.bat` 和 `scripts/health-check.sh`
    - 检查所有服务状态
    - _Requirements: 5.4_

- [x] 7. 创建部署文档
  - [x] 7.1 创建 README 部署说明文档
    - 编写完整的部署步骤说明
    - 包含前置条件、快速开始、常见问题
    - _Requirements: 6.5_

- [x] 8. Final Checkpoint - 完整验证
  - 执行完整的部署流程验证
  - 验证前后端联调正常
  - 验证登录功能正常（admin/admin123）
  - 确保所有脚本可正常执行

## Notes

- 所有脚本同时提供 Windows (.bat) 和 Linux/Mac (.sh) 版本
- 数据库初始化脚本仅在首次启动时执行
- 默认配置开箱即用，无需额外修改
- 修改 Java 代码后运行 rebuild-backend 脚本即可
- 修改前端代码会自动热更新
