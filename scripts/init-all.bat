@echo off
chcp 65001 >nul

:: 获取脚本所在目录并切换到项目根目录
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%.."

echo ========================================
echo RuoYi-Vue-Plus 本地环境一键初始化
echo ========================================
echo.

:: 检查 Git
echo [1/7] 检查 Git 环境...
git --version >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到 Git，请先安装 Git
    pause
    exit /b 1
)
echo Git 环境正常

:: 检查 Docker
echo.
echo [2/7] 检查 Docker 环境...
docker info >nul 2>&1
if errorlevel 1 (
    echo 错误：Docker 未运行，请先启动 Docker Desktop
    pause
    exit /b 1
)
echo Docker 环境正常

:: 克隆后端项目
echo.
echo [3/7] 克隆后端项目...
if exist "RuoYi-Vue-Plus" (
    echo 后端项目已存在，跳过克隆
) else (
    echo 正在克隆 RuoYi-Vue-Plus...
    git clone https://gitee.com/dromara/RuoYi-Vue-Plus.git
    if errorlevel 1 (
        echo 克隆失败，尝试使用 GitHub 源...
        git clone https://github.com/dromara/RuoYi-Vue-Plus.git
    )
)

:: 复制后端配置文件
echo.
echo [4/7] 配置后端项目...
if exist "RuoYi-Vue-Plus" (
    if exist "config\application-local.yml" (
        copy /Y "config\application-local.yml" "RuoYi-Vue-Plus\ruoyi-admin\src\main\resources\application-local.yml" >nul
        echo 已复制 application-local.yml 到后端项目
    )
)

:: 克隆前端项目
echo.
echo [5/7] 克隆前端项目...
if exist "plus-ui" (
    echo 前端项目已存在，跳过克隆
) else (
    echo 正在克隆 plus-ui...
    git clone https://gitee.com/JavaLionLi/plus-ui.git
    if errorlevel 1 (
        echo 克隆失败，尝试使用 GitHub 源...
        git clone https://github.com/JavaLionLi/plus-ui.git
    )
)

:: 下载 SQL 脚本
echo.
echo [6/7] 下载 SQL 初始化脚本...
call "%SCRIPT_DIR%download-sql.bat"

:: 启动基础设施
echo.
echo [7/7] 启动 Docker 基础设施...
docker-compose -f docker\docker-compose.yml up -d

:: 等待 MySQL 就绪
echo.
echo 等待 MySQL 就绪（约 30 秒）...
timeout /t 30 /nobreak >nul

:: 导入健康管理模块 SQL
echo.
echo [8/8] 导入健康管理模块数据...
if exist "sql\health_management.sql" (
    docker cp sql\health_management.sql ruoyi-mysql:/tmp/health_management.sql
    docker exec ruoyi-mysql mysql -uroot -proot --default-character-set=utf8mb4 ry-vue -e "source /tmp/health_management.sql"
    if errorlevel 1 (
        echo 警告：健康管理 SQL 导入失败，请手动执行 sql\health_management.sql
    ) else (
        echo 健康管理模块数据导入成功
    )
) else (
    echo 警告：未找到 sql\health_management.sql
)

:: 检查服务状态
echo.
echo 检查服务状态...
docker-compose -f docker\docker-compose.yml ps

echo.
echo ========================================
echo 初始化完成！
echo ========================================
echo.
echo 基础设施服务地址：
echo   MySQL:  localhost:3306 (root/root)
echo   Redis:  localhost:6379 (密码: ruoyi123)
echo   MinIO:  localhost:9000 (ruoyi/ruoyi123)
echo.
echo 启动后端服务：
echo   cd RuoYi-Vue-Plus
echo   mvn spring-boot:run -pl ruoyi-admin -Dspring-boot.run.profiles=local
echo.
echo 启动前端服务（新开终端）：
echo   cd plus-ui
echo   npm install
echo   npm run dev
echo.
echo 默认登录账号: admin / admin123
echo.
pause
