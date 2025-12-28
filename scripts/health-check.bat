@echo off
chcp 65001 >nul
echo ========================================
echo RuoYi-Vue-Plus 服务健康检查
echo ========================================
echo.

set PASS=0
set FAIL=0

:: 检查 MySQL
echo [MySQL] 检查 localhost:3306 ...
powershell -Command "try { $tcp = New-Object System.Net.Sockets.TcpClient('localhost', 3306); $tcp.Close(); exit 0 } catch { exit 1 }" >nul 2>&1
if errorlevel 1 (
    echo [MySQL] FAILED - 无法连接
    set /a FAIL+=1
) else (
    echo [MySQL] OK
    set /a PASS+=1
)

:: 检查 Redis
echo [Redis] 检查 localhost:6379 ...
powershell -Command "try { $tcp = New-Object System.Net.Sockets.TcpClient('localhost', 6379); $tcp.Close(); exit 0 } catch { exit 1 }" >nul 2>&1
if errorlevel 1 (
    echo [Redis] FAILED - 无法连接
    set /a FAIL+=1
) else (
    echo [Redis] OK
    set /a PASS+=1
)

:: 检查 MinIO API
echo [MinIO API] 检查 localhost:9000 ...
powershell -Command "try { $tcp = New-Object System.Net.Sockets.TcpClient('localhost', 9000); $tcp.Close(); exit 0 } catch { exit 1 }" >nul 2>&1
if errorlevel 1 (
    echo [MinIO API] FAILED - 无法连接
    set /a FAIL+=1
) else (
    echo [MinIO API] OK
    set /a PASS+=1
)

:: 检查 MinIO Console
echo [MinIO Console] 检查 localhost:9001 ...
powershell -Command "try { $tcp = New-Object System.Net.Sockets.TcpClient('localhost', 9001); $tcp.Close(); exit 0 } catch { exit 1 }" >nul 2>&1
if errorlevel 1 (
    echo [MinIO Console] FAILED - 无法连接
    set /a FAIL+=1
) else (
    echo [MinIO Console] OK
    set /a PASS+=1
)

:: 检查后端服务
echo [Backend] 检查 localhost:8080 ...
powershell -Command "try { $tcp = New-Object System.Net.Sockets.TcpClient('localhost', 8080); $tcp.Close(); exit 0 } catch { exit 1 }" >nul 2>&1
if errorlevel 1 (
    echo [Backend] FAILED - 未启动或无法连接
    set /a FAIL+=1
) else (
    echo [Backend] OK
    set /a PASS+=1
)

echo.
echo ========================================
echo 检查结果: %PASS% 通过, %FAIL% 失败
echo ========================================
echo.

if %FAIL% GTR 0 (
    echo 提示：部分服务未启动，请检查：
    echo   - Docker 服务是否启动: scripts\start-infra.bat
    echo   - 后端服务是否启动: scripts\start-backend.bat
)

pause
