#!/bin/bash
echo "========================================"
echo "下载 RuoYi-Vue-Plus SQL 初始化脚本"
echo "========================================"

SQL_DIR="docker/mysql/init"
BASE_URL="https://raw.githubusercontent.com/dromara/RuoYi-Vue-Plus/5.X/script/sql"

echo ""
echo "正在下载 SQL 脚本到 $SQL_DIR 目录..."
echo ""

# 创建目录
mkdir -p $SQL_DIR

# 下载核心 SQL 文件
echo "下载 ry_vue_5.X.sql ..."
curl -L -o "$SQL_DIR/01_ry_vue_5.X.sql" "$BASE_URL/ry_vue_5.X.sql"

echo "下载 ry_job.sql ..."
curl -L -o "$SQL_DIR/02_ry_job.sql" "$BASE_URL/ry_job.sql"

echo "下载 ry_workflow.sql ..."
curl -L -o "$SQL_DIR/03_ry_workflow.sql" "$BASE_URL/ry_workflow.sql"

echo ""
echo "========================================"
echo "SQL 脚本下载完成！"
echo "========================================"
echo ""
echo "文件列表："
ls -la $SQL_DIR/*.sql
