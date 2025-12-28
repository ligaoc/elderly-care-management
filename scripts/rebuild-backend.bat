@echo off
chcp 65001 >nul
echo ========================================
echo 重构建 RuoYi-Vue-Plus 后端
echo ========================================

:: 检查是否在项目根目录
if not exist "pom.xml" (
    echo 错误：请在 RuoYi-Vue-Plus 项目根目录下运行此脚本
    pause
    exit /b 1
)

echo.
echo 正在清理并重新构建项目...
echo.

:: 清理并构建
mvn clean package -DskipTests

if errorlevel 1 (
    echo.
    echo 构建失败！请检查错误信息
    pause
    exit /b 1
)

echo.
echo ========================================
echo 构建完成！
echo ========================================
echo.
echo JAR 文件位置: ruoyi-admin\target\ruoyi-admin.jar
echo.
echo 启动命令:
echo java -jar ruoyi-admin\target\ruoyi-admin.jar --spring.profiles.active=local
echo.
echo 或使用脚本启动:
echo scripts\start-backend.bat
echo.
pause
