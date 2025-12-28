@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul

:: 获取脚本所在目录并切换到项目根目录
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%.."

echo ========================================
echo RuoYi-Vue-Plus 一键启动
echo ========================================
echo.

:: 检查必要目录
if not exist "RuoYi-Vue-Plus" (
    echo 错误：后端项目 RuoYi-Vue-Plus 不存在
    echo 请先执行 scripts\init-all.bat 初始化环境
    pause
    exit /b 1
)

if not exist "plus-ui" (
    echo 错误：前端项目 plus-ui 不存在
    echo 请先执行 scripts\init-all.bat 初始化环境
    pause
    exit /b 1
)

:: [1/3] 启动基础设施
echo [1/3] 启动 Docker 基础设施...
docker-compose -f docker\docker-compose.yml up -d
if errorlevel 1 (
    echo 错误：Docker 启动失败，请检查 Docker Desktop 是否运行
    pause
    exit /b 1
)
echo Docker 基础设施已启动

:: 检查 MySQL 是否就绪
echo.
echo 检查 MySQL 状态...
docker exec ruoyi-mysql mysqladmin ping -h localhost -u root -proot >nul 2>&1
if errorlevel 1 (
    echo MySQL 正在启动，等待就绪...
    set RETRY=0
    :WAIT_MYSQL
    set /a RETRY+=1
    if !RETRY! GTR 30 (
        echo 警告：MySQL 启动超时，继续执行...
        goto START_BACKEND
    )
    timeout /t 2 /nobreak >nul
    docker exec ruoyi-mysql mysqladmin ping -h localhost -u root -proot >nul 2>&1
    if errorlevel 1 goto WAIT_MYSQL
)
echo MySQL 已就绪

:START_BACKEND
:: [2/3] 启动后端（新窗口）
echo.
echo [2/3] 启动后端服务（新窗口）...

:: 检查是否需要首次构建
if not exist "RuoYi-Vue-Plus\ruoyi-admin\target\ruoyi-admin.jar" (
    echo 检测到首次运行，需要先构建项目（约 3-5 分钟）...
    start "RuoYi-Backend" cmd /k "cd /d "%cd%\RuoYi-Vue-Plus" && echo 正在构建项目... && mvn clean install -DskipTests && echo 构建完成，启动服务... && mvn spring-boot:run -pl ruoyi-admin -Dspring-boot.run.profiles=local"
) else (
    start "RuoYi-Backend" cmd /k "cd /d "%cd%\RuoYi-Vue-Plus" && echo 正在启动后端服务... && mvn spring-boot:run -pl ruoyi-admin -Dspring-boot.run.profiles=local"
)
echo 后端服务正在启动（新窗口）

:: [3/3] 启动前端（新窗口）
echo.
echo [3/3] 启动前端服务（新窗口）...
set FRONTEND_PATH=%cd%\plus-ui
start "RuoYi-Frontend" cmd /k "cd /d %FRONTEND_PATH% && echo 正在启动前端服务... && npm run dev"
echo 前端服务正在启动（新窗口）

echo.
echo ========================================
echo 所有服务已启动！
echo ========================================
echo.
echo 服务地址：
echo   前端: http://localhost:80
echo   后端: http://localhost:8080
echo   MySQL: localhost:3306
echo   Redis: localhost:6379
echo   MinIO: http://localhost:9001
echo.
echo 默认账号: admin / admin123
echo.
echo 关闭所有服务: scripts\stop-all.bat
echo.
pause
