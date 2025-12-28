# Requirements Document

## Introduction

本文档定义了 RuoYi-Vue-Plus 项目本地部署的需求规范。目标是通过 Docker 容器化方式部署基础设施（MySQL、Redis、MinIO），同时以源码方式运行 Java 后端和 Vue 前端，实现开发环境的快速搭建和代码热更新。

## Glossary

- **Docker_Compose**: Docker 容器编排工具，用于定义和运行多容器应用
- **MySQL**: 关系型数据库，存储系统业务数据
- **Redis**: 内存缓存数据库，用于会话管理和缓存
- **MinIO**: 对象存储服务，用于文件存储
- **Backend_Server**: RuoYi-Vue-Plus Java 后端服务
- **Frontend_App**: plus-ui Vue3 前端应用
- **SQL_Scripts**: 数据库初始化脚本，包含表结构和初始数据

## Requirements

### Requirement 1: Docker 基础设施部署

**User Story:** 作为开发者，我希望通过 Docker Compose 一键启动所有基础设施服务，以便快速搭建开发环境。

#### Acceptance Criteria

1. WHEN 执行 docker-compose up 命令 THEN Docker_Compose SHALL 启动 MySQL 8.0 容器并暴露 3306 端口
2. WHEN 执行 docker-compose up 命令 THEN Docker_Compose SHALL 启动 Redis 7.x 容器并暴露 6379 端口
3. WHEN 执行 docker-compose up 命令 THEN Docker_Compose SHALL 启动 MinIO 容器并暴露 9000 和 9001 端口
4. WHILE 容器运行中 THEN Docker_Compose SHALL 将数据持久化到本地挂载目录
5. IF 容器启动失败 THEN Docker_Compose SHALL 输出错误日志信息

### Requirement 2: 数据库自动初始化

**User Story:** 作为开发者，我希望数据库在首次启动时自动初始化表结构和数据，以便无需手动执行 SQL 脚本。

#### Acceptance Criteria

1. WHEN MySQL 容器首次启动 THEN SQL_Scripts SHALL 自动创建 ry-vue 数据库
2. WHEN MySQL 容器首次启动 THEN SQL_Scripts SHALL 自动执行 ry_vue_5.X.sql 初始化系统表
3. WHEN MySQL 容器首次启动 THEN SQL_Scripts SHALL 自动执行 ry_job.sql 初始化定时任务表
4. WHEN MySQL 容器首次启动 THEN SQL_Scripts SHALL 自动执行 ry_workflow.sql 初始化工作流表
5. IF SQL 脚本执行失败 THEN MySQL SHALL 记录错误日志并保持容器运行

### Requirement 3: 后端源码运行

**User Story:** 作为开发者，我希望以源码方式运行 Java 后端，以便修改代码后能快速重新构建和启动。

#### Acceptance Criteria

1. WHEN 执行 Maven 构建命令 THEN Backend_Server SHALL 编译所有模块并生成可执行 JAR
2. WHEN 启动后端服务 THEN Backend_Server SHALL 连接到 Docker 中的 MySQL 数据库
3. WHEN 启动后端服务 THEN Backend_Server SHALL 连接到 Docker 中的 Redis 缓存
4. WHEN 启动后端服务 THEN Backend_Server SHALL 连接到 Docker 中的 MinIO 存储
5. WHEN 后端服务启动成功 THEN Backend_Server SHALL 在 8080 端口提供 API 服务
6. IF 数据库连接失败 THEN Backend_Server SHALL 输出连接错误信息并退出

### Requirement 4: 前端源码运行

**User Story:** 作为开发者，我希望以源码方式运行 Vue 前端，以便修改代码后能实时预览效果。

#### Acceptance Criteria

1. WHEN 执行 npm install 命令 THEN Frontend_App SHALL 安装所有依赖包
2. WHEN 执行 npm run dev 命令 THEN Frontend_App SHALL 启动开发服务器
3. WHEN 前端开发服务器启动 THEN Frontend_App SHALL 在 80 端口提供访问
4. WHEN 前端代码修改 THEN Frontend_App SHALL 自动热更新页面
5. WHEN 前端请求后端 API THEN Frontend_App SHALL 正确代理到后端 8080 端口

### Requirement 5: 代码重构建流程

**User Story:** 作为开发者，我希望修改代码后只需简单命令即可重新构建，以便提高开发效率。

#### Acceptance Criteria

1. WHEN 修改 Java 代码后执行重构建命令 THEN Backend_Server SHALL 重新编译并启动服务
2. WHEN 修改前端代码 THEN Frontend_App SHALL 自动检测变更并热更新
3. WHEN 需要完整重构建 THEN 系统 SHALL 提供一键重构建脚本
4. WHILE 重构建过程中 THEN 系统 SHALL 保持基础设施服务运行不中断

### Requirement 6: 配置管理

**User Story:** 作为开发者，我希望所有配置集中管理且易于修改，以便适配不同的本地环境。

#### Acceptance Criteria

1. THE 系统 SHALL 提供统一的环境配置文件管理数据库连接信息
2. THE 系统 SHALL 提供统一的环境配置文件管理 Redis 连接信息
3. THE 系统 SHALL 提供统一的环境配置文件管理 MinIO 连接信息
4. WHEN 配置文件修改 THEN 系统 SHALL 在服务重启后生效新配置
5. THE 系统 SHALL 使用默认配置值确保开箱即用
