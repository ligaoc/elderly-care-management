# SQL 文件编码修复脚本
# 将 SQL 文件从 GBK/GB2312 转换为 UTF-8 编码（无 BOM）

$sqlDir = "..\docker\mysql\init"
$sqlFiles = Get-ChildItem -Path $sqlDir -Filter "*.sql"

# 注册 GBK 编码
[System.Text.Encoding]::RegisterProvider([System.Text.CodePagesEncodingProvider]::Instance)
$gbkEncoding = [System.Text.Encoding]::GetEncoding("GBK")
$utf8NoBom = [System.Text.UTF8Encoding]::new($false)

foreach ($file in $sqlFiles) {
    Write-Host "Processing: $($file.Name)"
    
    # 读取原始字节
    $bytes = [System.IO.File]::ReadAllBytes($file.FullName)
    
    # 检测是否有 UTF-8 BOM
    $hasBom = ($bytes.Length -ge 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF)
    
    if ($hasBom) {
        # 移除 BOM 后按 UTF-8 读取
        $content = [System.Text.Encoding]::UTF8.GetString($bytes, 3, $bytes.Length - 3)
        Write-Host "  - Detected UTF-8 with BOM, removing BOM"
    } else {
        # 尝试检测编码：如果包含高位字节，可能是 GBK
        $hasHighBytes = $false
        foreach ($b in $bytes) {
            if ($b -gt 127) {
                $hasHighBytes = $true
                break
            }
        }
        
        if ($hasHighBytes) {
            # 尝试用 GBK 解码
            $gbkContent = $gbkEncoding.GetString($bytes)
            $utf8Content = [System.Text.Encoding]::UTF8.GetString($bytes)
            
            # 检查哪个解码结果更合理（GBK 解码后应该有正常的中文字符）
            if ($gbkContent -match '[\u4e00-\u9fff]' -and -not ($utf8Content -match '[\u4e00-\u9fff]')) {
                $content = $gbkContent
                Write-Host "  - Detected GBK encoding, converting to UTF-8"
            } else {
                $content = $utf8Content
                Write-Host "  - Detected UTF-8 encoding (no BOM)"
            }
        } else {
            # 纯 ASCII
            $content = [System.Text.Encoding]::UTF8.GetString($bytes)
            Write-Host "  - ASCII only content"
        }
    }
    
    # 写入为 UTF-8 无 BOM
    [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
    Write-Host "  - Saved as UTF-8 (no BOM)"
}

Write-Host "`nAll SQL files have been processed."
Write-Host "Please restart Docker to reinitialize the database."
