@echo off
chcp 65001 >nul

:: 获取脚本所在目录并切换到项目根目录
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%.."

echo ========================================
echo RuoYi-Vue-Plus 一键关闭
echo ========================================
echo.

:: [1/3] 关闭前端
echo [1/3] 关闭前端服务...
taskkill /FI "WINDOWTITLE eq RuoYi-Frontend*" /F >nul 2>&1
:: 关闭 node 进程（前端开发服务器）
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":80 " ^| findstr "LISTENING"') do (
    taskkill /PID %%a /F >nul 2>&1
)
echo 前端服务已关闭

:: [2/3] 关闭后端
echo.
echo [2/3] 关闭后端服务...
taskkill /FI "WINDOWTITLE eq RuoYi-Backend*" /F >nul 2>&1
:: 关闭 Java 进程（后端服务）
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8080 " ^| findstr "LISTENING"') do (
    taskkill /PID %%a /F >nul 2>&1
)
echo 后端服务已关闭

:: [3/3] 关闭 Docker 基础设施
echo.
echo [3/3] 关闭 Docker 基础设施...
docker-compose -f docker\docker-compose.yml down >nul 2>&1
echo Docker 基础设施已关闭

echo.
echo ========================================
echo 所有服务已关闭！
echo ========================================
echo.
echo 数据已持久化，下次启动数据不会丢失
echo 重新启动: scripts\start-all.bat
echo.
pause
