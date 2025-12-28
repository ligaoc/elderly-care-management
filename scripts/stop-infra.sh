#!/bin/bash
echo "========================================"
echo "停止 Docker 基础设施服务"
echo "========================================"

cd docker

echo ""
echo "正在停止服务..."
echo ""

docker-compose down

echo ""
echo "服务已停止"
echo ""
echo "注意：数据已持久化到 docker 目录下，下次启动数据不会丢失"
