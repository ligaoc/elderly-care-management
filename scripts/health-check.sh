#!/bin/bash
echo "========================================"
echo "RuoYi-Vue-Plus 服务健康检查"
echo "========================================"
echo ""

PASS=0
FAIL=0

# 检查端口函数
check_port() {
    local name=$1
    local host=$2
    local port=$3
    
    echo -n "[$name] 检查 $host:$port ... "
    
    if nc -z $host $port 2>/dev/null; then
        echo "OK"
        ((PASS++))
        return 0
    else
        echo "FAILED - 无法连接"
        ((FAIL++))
        return 1
    fi
}

# 检查各服务
check_port "MySQL" "localhost" 3306
check_port "Redis" "localhost" 6379
check_port "MinIO API" "localhost" 9000
check_port "MinIO Console" "localhost" 9001
check_port "Backend" "localhost" 8080

echo ""
echo "========================================"
echo "检查结果: $PASS 通过, $FAIL 失败"
echo "========================================"
echo ""

if [ $FAIL -gt 0 ]; then
    echo "提示：部分服务未启动，请检查："
    echo "  - Docker 服务是否启动: ./scripts/start-infra.sh"
    echo "  - 后端服务是否启动: ./scripts/start-backend.sh"
fi
