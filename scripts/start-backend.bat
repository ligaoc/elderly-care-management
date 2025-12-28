@echo off
chcp 65001 >nul
echo ========================================
echo 启动 RuoYi-Vue-Plus 后端服务
echo ========================================

:: 检查是否在项目根目录
if not exist "pom.xml" (
    echo 错误：请在 RuoYi-Vue-Plus 项目根目录下运行此脚本
    echo 当前目录：%cd%
    pause
    exit /b 1
)

:: 检查 Java 环境
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到 Java 环境，请安装 JDK 17 或更高版本
    pause
    exit /b 1
)

:: 检查 Maven 环境
mvn -version >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到 Maven 环境，请安装 Maven
    pause
    exit /b 1
)

echo.
echo 使用 local 配置文件启动后端服务...
echo 后端服务将在 http://localhost:8080 启动
echo.

:: 使用 Maven 启动
mvn spring-boot:run -pl ruoyi-admin -Dspring-boot.run.profiles=local

pause
