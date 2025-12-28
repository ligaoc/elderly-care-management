@echo off
chcp 65001 >nul
echo ========================================
echo 启动 RuoYi-Vue-Plus 前端服务
echo ========================================

:: 设置前端项目目录（默认为 plus-ui）
set FRONTEND_DIR=plus-ui

:: 检查前端目录是否存在
if not exist "%FRONTEND_DIR%" (
    echo 前端目录 %FRONTEND_DIR% 不存在
    echo.
    echo 请先克隆前端项目：
    echo git clone https://gitee.com/JavaLionLi/plus-ui.git
    echo 或
    echo git clone https://github.com/JavaLionLi/plus-ui.git
    echo.
    pause
    exit /b 1
)

:: 检查 Node.js 环境
node -v >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到 Node.js 环境，请安装 Node.js 18+
    pause
    exit /b 1
)

cd %FRONTEND_DIR%

:: 检查是否已安装依赖
if not exist "node_modules" (
    echo 正在安装依赖...
    call npm install
)

echo.
echo 启动前端开发服务器...
echo 前端服务将在 http://localhost:80 启动
echo.

:: 启动开发服务器
call npm run dev

pause
