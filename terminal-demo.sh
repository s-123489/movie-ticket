#!/bin/bash

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color

API_BASE="http://localhost:8090"

# 清屏函数
clear_screen() {
    clear
    echo -e "${PURPLE}════════════════════════════════════════════════════════${NC}"
    echo -e "${WHITE}    🎬 电影票务管理系统 - 终端演示${NC}"
    echo -e "${PURPLE}════════════════════════════════════════════════════════${NC}"
    echo ""
}

# 显示菜单
show_menu() {
    clear_screen
    echo -e "${CYAN}请选择要测试的功能：${NC}"
    echo ""
    echo -e "${GREEN}1.${NC} 👥 测试用户服务"
    echo -e "${GREEN}2.${NC} 🎥 测试电影服务"
    echo -e "${GREEN}3.${NC} 🎫 测试票务服务"
    echo -e "${GREEN}4.${NC} 💳 测试支付服务"
    echo -e "${GREEN}5.${NC} ⭐ 测试推荐服务"
    echo -e "${GREEN}6.${NC} 🏥 检查所有服务健康状态"
    echo -e "${GREEN}7.${NC} 🚀 运行完整演示"
    echo -e "${GREEN}0.${NC} 退出"
    echo ""
    echo -ne "${YELLOW}请输入选项 [0-7]: ${NC}"
}

# API 调用函数
call_api() {
    local url=$1
    local title=$2

    echo -e "\n${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${WHITE}📡 请求: ${CYAN}GET ${url}${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}\n"

    response=$(curl -s "$url")

    if [ -n "$response" ]; then
        echo "$response" | python3 -m json.tool 2>/dev/null || echo "$response"
        echo -e "\n${GREEN}✅ 请求成功${NC}"
    else
        echo -e "${RED}❌ 请求失败或无响应${NC}"
    fi

    echo -e "\n${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
}

# 暂停函数
pause() {
    echo -e "\n${YELLOW}按 Enter 继续...${NC}"
    read
}

# 测试用户服务
test_users() {
    clear_screen
    echo -e "${CYAN}═══════════════════ 👥 用户服务测试 ═══════════════════${NC}\n"

    echo -e "${WHITE}1. 获取所有用户${NC}"
    call_api "$API_BASE/api/users"
    pause

    clear_screen
    echo -e "${CYAN}═══════════════════ 👥 用户服务测试 ═══════════════════${NC}\n"

    echo -e "${WHITE}2. 健康检查${NC}"
    call_api "http://localhost:8081/api/health"
    pause
}

# 测试电影服务
test_movies() {
    clear_screen
    echo -e "${CYAN}═══════════════════ 🎥 电影服务测试 ═══════════════════${NC}\n"

    echo -e "${WHITE}1. 获取所有电影${NC}"
    call_api "$API_BASE/api/movies"
    pause

    clear_screen
    echo -e "${CYAN}═══════════════════ 🎥 电影服务测试 ═══════════════════${NC}\n"

    echo -e "${WHITE}2. 获取正在上映的电影${NC}"
    call_api "$API_BASE/api/movies?showing=true"
    pause
}

# 测试票务服务
test_tickets() {
    clear_screen
    echo -e "${CYAN}═══════════════════ 🎫 票务服务测试 ═══════════════════${NC}\n"

    echo -e "${WHITE}1. 获取所有票务${NC}"
    call_api "$API_BASE/api/tickets"
    pause

    clear_screen
    echo -e "${CYAN}═══════════════════ 🎫 票务服务测试 ═══════════════════${NC}\n"

    echo -e "${WHITE}2. 获取排片信息${NC}"
    call_api "$API_BASE/api/showtimes"
    pause
}

# 测试支付服务
test_payments() {
    clear_screen
    echo -e "${CYAN}═══════════════════ 💳 支付服务测试 ═══════════════════${NC}\n"

    echo -e "${WHITE}获取所有支付记录${NC}"
    call_api "$API_BASE/api/payments"
    pause
}

# 测试推荐服务
test_recommendations() {
    clear_screen
    echo -e "${CYAN}═══════════════════ ⭐ 推荐服务测试 ═══════════════════${NC}\n"

    echo -e "${WHITE}获取热门推荐${NC}"
    call_api "$API_BASE/api/recommendations/popular"
    pause
}

# 健康检查
health_check() {
    clear_screen
    echo -e "${CYAN}═══════════════════ 🏥 服务健康检查 ═══════════════════${NC}\n"

    services=(
        "用户服务:http://localhost:8081/api/health"
        "电影服务:http://localhost:8082/api/movies"
        "票务服务:http://localhost:8083/api/tickets"
        "支付服务:http://localhost:8084/api/payments"
        "推荐服务:http://localhost:8085/api/recommendations/popular"
        "网关服务:http://localhost:8090/api/users"
    )

    for service in "${services[@]}"; do
        IFS=':' read -r name url <<< "$service"
        echo -ne "${WHITE}检查 $name ... ${NC}"

        if curl -s -f "$url" > /dev/null 2>&1; then
            echo -e "${GREEN}✅ 在线${NC}"
        else
            echo -e "${RED}❌ 离线${NC}"
        fi
    done

    pause
}

# 完整演示
full_demo() {
    clear_screen
    echo -e "${PURPLE}════════════════════════════════════════════════════════${NC}"
    echo -e "${WHITE}           🚀 开始完整系统演示${NC}"
    echo -e "${PURPLE}════════════════════════════════════════════════════════${NC}\n"

    sleep 2

    # 1. 用户服务
    clear_screen
    echo -e "${CYAN}【演示 1/5】 👥 用户服务${NC}\n"
    call_api "$API_BASE/api/users"
    sleep 3

    # 2. 电影服务
    clear_screen
    echo -e "${CYAN}【演示 2/5】 🎥 电影服务${NC}\n"
    call_api "$API_BASE/api/movies"
    sleep 3

    # 3. 票务服务
    clear_screen
    echo -e "${CYAN}【演示 3/5】 🎫 票务服务${NC}\n"
    call_api "$API_BASE/api/tickets"
    sleep 3

    # 4. 支付服务
    clear_screen
    echo -e "${CYAN}【演示 4/5】 💳 支付服务${NC}\n"
    call_api "$API_BASE/api/payments"
    sleep 3

    # 5. 推荐服务
    clear_screen
    echo -e "${CYAN}【演示 5/5】 ⭐ 推荐服务${NC}\n"
    call_api "$API_BASE/api/recommendations/popular"
    sleep 3

    clear_screen
    echo -e "${GREEN}════════════════════════════════════════════════════════${NC}"
    echo -e "${WHITE}           ✅ 演示完成！${NC}"
    echo -e "${GREEN}════════════════════════════════════════════════════════${NC}\n"
    pause
}

# 主循环
main() {
    while true; do
        show_menu
        read choice

        case $choice in
            1) test_users ;;
            2) test_movies ;;
            3) test_tickets ;;
            4) test_payments ;;
            5) test_recommendations ;;
            6) health_check ;;
            7) full_demo ;;
            0)
                clear_screen
                echo -e "${GREEN}👋 再见！${NC}\n"
                exit 0
                ;;
            *)
                echo -e "${RED}无效选项，请重试${NC}"
                sleep 1
                ;;
        esac
    done
}

# 启动程序
main
