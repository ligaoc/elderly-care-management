@echo off
chcp 65001 >nul

:: 获取脚本所在目录并切换到项目根目录
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%.."

echo ========================================
echo 启动 Docker 基础设施服务
echo ========================================

:: 检查 Docker 是否运行
docker info >nul 2>&1
if errorlevel 1 (
    echo 错误：Docker 未运行，请先启动 Docker Desktop
    pause
    exit /b 1
)

echo.
echo 正在启动 MySQL、Redis、MinIO 服务...
echo.

docker-compose -f docker\docker-compose.yml up -d

echo.
echo ========================================
echo 服务启动完成！
echo ========================================
echo.
echo MySQL:  localhost:3306 (root/root)
echo Redis:  localhost:6379 (密码: ruoyi123)
echo MinIO:  localhost:9000 (ruoyi/ruoyi123)
echo MinIO Console: localhost:9001
echo.
echo 查看服务状态: docker-compose ps
echo 查看日志: docker-compose logs -f
echo.
pause
