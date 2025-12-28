#!/bin/bash
echo "========================================"
echo "RuoYi-Vue-Plus 本地环境一键初始化"
echo "========================================"
echo ""

# 检查 Docker
echo "[1/5] 检查 Docker 环境..."
if ! docker info > /dev/null 2>&1; then
    echo "错误：Docker 未运行，请先启动 Docker"
    exit 1
fi
echo "Docker 环境正常"

# 下载 SQL 脚本
echo ""
echo "[2/5] 下载 SQL 初始化脚本..."
chmod +x scripts/download-sql.sh
./scripts/download-sql.sh

# 启动基础设施
echo ""
echo "[3/5] 启动 Docker 基础设施..."
cd docker
docker-compose up -d
cd ..

# 等待 MySQL 就绪
echo ""
echo "[4/5] 等待 MySQL 就绪（约 30 秒）..."
sleep 30

# 检查服务状态
echo ""
echo "[5/5] 检查服务状态..."
cd docker
docker-compose ps
cd ..

echo ""
echo "========================================"
echo "初始化完成！"
echo "========================================"
echo ""
echo "服务地址："
echo "  MySQL:  localhost:3306 (root/root)"
echo "  Redis:  localhost:6379 (密码: ruoyi123)"
echo "  MinIO:  localhost:9000 (ruoyi/ruoyi123)"
echo ""
echo "下一步："
echo "  1. 克隆后端项目: git clone https://gitee.com/dromara/RuoYi-Vue-Plus.git"
echo "  2. 复制配置文件: cp config/application-local.yml 到后端项目"
echo "  3. 启动后端: ./scripts/start-backend.sh"
echo "  4. 克隆前端项目: git clone https://gitee.com/JavaLionLi/plus-ui.git"
echo "  5. 启动前端: ./scripts/start-frontend.sh"
echo ""
echo "默认登录账号: admin / admin123"
