#!/bin/bash
# 重新部署前端容器以应用新的 nginx 配置
# 使用方法: bash reload-frontend.sh

echo "================================================"
echo "🔄 重新部署前端容器"
echo "================================================"
echo ""

# 停止并删除现有前端容器
echo "📦 停止现有前端容器..."
docker stop cinema-frontend 2>/dev/null
docker rm cinema-frontend 2>/dev/null

echo ""
echo "🔨 重新构建前端镜像..."
cd "$(dirname "$0")"
docker build -t cinema-frontend:1.0.0 .

if [ $? -ne 0 ]; then
    echo "❌ 构建失败！"
    exit 1
fi

echo ""
echo "🚀 启动新的前端容器..."
docker run -d \
    --name cinema-frontend \
    --network movie-ticket-system_cinema-network \
    -p 80:80 \
    --restart unless-stopped \
    cinema-frontend:1.0.0

if [ $? -ne 0 ]; then
    echo "❌ 启动失败！"
    exit 1
fi

echo ""
echo "✅ 前端容器重新部署成功！"
echo ""
echo "================================================"
echo "📋 测试步骤:"
echo "================================================"
echo "1. 清除浏览器 HSTS 缓存 (chrome://net-internals/#hsts)"
echo "2. 访问测试页面: http://localhost/test-http.html"
echo "3. 查看响应头:"
echo "   curl -I http://localhost"
echo ""
echo "预期看到以下响应头:"
echo "  - Strict-Transport-Security: max-age=0"
echo "  - X-Protocol: http"
echo "  - X-Served-By: Nginx-HTTP"
echo "================================================"
echo ""

# 显示容器日志的最后几行
echo "📄 容器日志:"
docker logs --tail 10 cinema-frontend

echo ""
echo "🔍 容器状态:"
docker ps | grep cinema-frontend
