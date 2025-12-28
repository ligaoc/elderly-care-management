@echo off
chcp 65001 >nul

:: 获取脚本所在目录并切换到项目根目录
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%.."

echo ========================================
echo 停止 Docker 基础设施服务
echo ========================================

echo.
echo 正在停止服务...
echo.

docker-compose -f docker\docker-compose.yml down

echo.
echo 服务已停止
echo.
echo 注意：数据已持久化到 docker 目录下，下次启动数据不会丢失
echo.
pause
