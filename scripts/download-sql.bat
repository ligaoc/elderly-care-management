@echo off
chcp 65001 >nul

:: 获取脚本所在目录并切换到项目根目录
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%.."

echo ========================================
echo 下载 RuoYi-Vue-Plus SQL 初始化脚本
echo ========================================

set SQL_DIR=docker\mysql\init
set BASE_URL=https://raw.githubusercontent.com/dromara/RuoYi-Vue-Plus/5.X/script/sql

:: 检查是否已下载
if exist "%SQL_DIR%\01_ry_vue_5.X.sql" (
    if exist "%SQL_DIR%\02_ry_job.sql" (
        if exist "%SQL_DIR%\03_ry_workflow.sql" (
            echo SQL 脚本已存在，跳过下载
            goto :eof
        )
    )
)

echo.
echo 正在下载 SQL 脚本到 %SQL_DIR% 目录...
echo.

:: 创建目录
if not exist %SQL_DIR% mkdir %SQL_DIR%

:: 下载核心 SQL 文件
echo 下载 ry_vue_5.X.sql ...
curl -L -o "%SQL_DIR%\01_ry_vue_5.X.sql" "%BASE_URL%/ry_vue_5.X.sql"

echo 下载 ry_job.sql ...
curl -L -o "%SQL_DIR%\02_ry_job.sql" "%BASE_URL%/ry_job.sql"

echo 下载 ry_workflow.sql ...
curl -L -o "%SQL_DIR%\03_ry_workflow.sql" "%BASE_URL%/ry_workflow.sql"

echo.
echo SQL 脚本下载完成！
