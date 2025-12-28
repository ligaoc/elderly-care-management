-- RuoYi-Vue-Plus 数据库初始化脚本
-- 此脚本会在 MySQL 容器首次启动时自动执行

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `ry-vue` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `ry-vue`;

-- 提示：完整的 SQL 脚本需要从 RuoYi-Vue-Plus 仓库下载
-- 下载地址：https://github.com/dromara/RuoYi-Vue-Plus/tree/5.X/script/sql
-- 
-- 需要下载以下文件并放入此目录：
-- 1. ry_vue_5.X.sql - 系统核心表
-- 2. ry_job.sql - 定时任务表（可选）
-- 3. ry_workflow.sql - 工作流表（可选）
--
-- 文件命名规则：按数字前缀排序执行
-- 例如：01_ry_vue_5.X.sql, 02_ry_job.sql, 03_ry_workflow.sql

SELECT 'RuoYi-Vue-Plus 数据库初始化脚本已执行' AS message;
