@echo off
chcp 65001 >nul

:: Get script directory and switch to project root
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%.."

echo ========================================
echo Import Demo Data for Tenant 461397
echo ========================================
echo.

:: Check Docker
echo Checking Docker...
docker info >nul 2>&1
if errorlevel 1 (
    echo Error: Docker is not running
    pause
    exit /b 1
)

:: Check MySQL container
echo Checking MySQL container...
docker ps --filter "name=ruoyi-mysql" --filter "status=running" | findstr ruoyi-mysql >nul
if errorlevel 1 (
    echo Error: ruoyi-mysql container is not running
    echo Please run: docker-compose -f docker\docker-compose.yml up -d
    pause
    exit /b 1
)

:: Copy SQL file to container
echo.
echo Copying SQL file to container...
docker cp sql\demo_data_tenant_461397.sql ruoyi-mysql:/tmp/demo_data_tenant_461397.sql
if errorlevel 1 (
    echo Error: Failed to copy SQL file
    pause
    exit /b 1
)

:: Execute SQL
echo.
echo Importing demo data...
docker exec ruoyi-mysql mysql -uroot -proot --default-character-set=utf8mb4 ry-vue -e "source /tmp/demo_data_tenant_461397.sql"
if errorlevel 1 (
    echo Error: SQL import failed
    pause
    exit /b 1
)

:: Verify data
echo.
echo Verifying imported data...
docker exec ruoyi-mysql mysql -uroot -proot --default-character-set=utf8mb4 ry-vue -e "SELECT 'Device Types' AS type, COUNT(*) AS count FROM health_device_type WHERE tenant_id='461397' UNION ALL SELECT 'Organizations', COUNT(*) FROM health_organization WHERE tenant_id='461397' UNION ALL SELECT 'Elders', COUNT(*) FROM health_elder WHERE tenant_id='461397' UNION ALL SELECT 'Devices', COUNT(*) FROM health_device WHERE tenant_id='461397' UNION ALL SELECT 'Vital Signs', COUNT(*) FROM health_vital_signs WHERE tenant_id='461397' UNION ALL SELECT 'Alerts', COUNT(*) FROM health_device_alert WHERE tenant_id='461397' UNION ALL SELECT 'Staff', COUNT(*) FROM health_service_staff WHERE tenant_id='461397';"

echo.
echo ========================================
echo Demo data import completed!
echo ========================================
echo.
echo Data Summary:
echo   - Device Types: 14
echo   - Organizations: 6
echo   - Elders: 15
echo   - Emergency Contacts: 20
echo   - Devices: 20 (16 online, 4 offline)
echo   - Vital Signs: 42 (7 days)
echo   - Alerts: 15 (4 pending, 2 processing, 8 handled, 1 ignored)
echo   - Service Staff: 10
echo.
pause
