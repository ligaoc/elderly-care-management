# 准备 SQL 文件脚本
# 从原始仓库复制 SQL 文件并添加字符集设置

$sourceDir = "..\RuoYi-Vue-Plus\script\sql"
$targetDir = "..\docker\mysql\init"

# UTF-8 无 BOM 编码
$utf8NoBom = [System.Text.UTF8Encoding]::new($false)

# 字符集设置前缀
$charsetPrefix = @"
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;
SET character_set_results=utf8mb4;
SET character_set_client=utf8mb4;

"@

# 文件映射
$files = @{
    "ry_vue_5.X.sql" = "01_ry_vue_5.X.sql"
    "ry_job.sql" = "02_ry_job.sql"
    "ry_workflow.sql" = "03_ry_workflow.sql"
}

foreach ($source in $files.Keys) {
    $sourcePath = Join-Path $sourceDir $source
    $targetPath = Join-Path $targetDir $files[$source]
    
    if (Test-Path $sourcePath) {
        Write-Host "Processing: $source -> $($files[$source])"
        
        # 读取原始文件（UTF-8）
        $content = [System.IO.File]::ReadAllText($sourcePath, [System.Text.Encoding]::UTF8)
        
        # 添加字符集前缀
        $newContent = $charsetPrefix + $content
        
        # 写入目标文件
        [System.IO.File]::WriteAllText($targetPath, $newContent, $utf8NoBom)
        
        Write-Host "  - Added charset settings and saved"
    } else {
        Write-Host "Warning: $sourcePath not found"
    }
}

Write-Host "`nSQL files prepared successfully."
