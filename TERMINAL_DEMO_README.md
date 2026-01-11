# 终端演示脚本使用指南

## 📋 文件说明

`terminal-demo.sh` - 交互式终端演示脚本，用于录制演示视频

## 🚀 快速开始

### 1. 在 Ubuntu 上同步并运行

```bash
# 挂载共享文件夹
sudo vmhgfs-fuse .host:/ /mnt/hgfs -o allow_other

# 复制脚本到桌面
cp /mnt/hgfs/share/terminal-demo.sh ~/桌面/

# 转换文件格式（重要！）
dos2unix ~/桌面/terminal-demo.sh
# 或者使用 sed
sed -i 's/\r$//' ~/桌面/terminal-demo.sh

# 添加执行权限
chmod +x ~/桌面/terminal-demo.sh

# 运行脚本
cd ~/桌面
./terminal-demo.sh
```

### 2. 使用方式

脚本启动后会显示菜单：

```
════════════════════════════════════════════════════════
    🎬 电影票务管理系统 - 终端演示
════════════════════════════════════════════════════════

请选择要测试的功能：

1. 👥 测试用户服务
2. 🎥 测试电影服务
3. 🎫 测试票务服务
4. 💳 测试支付服务
5. ⭐ 测试推荐服务
6. 🏥 检查所有服务健康状态
7. 🚀 运行完整演示
0. 退出

请输入选项 [0-7]:
```

## 🎥 录制演示视频

### 方法 1: Ubuntu 内置录屏（推荐）

```bash
# 1. 启动终端，全屏显示
# 2. 按 Ctrl+Alt+Shift+R 开始录制
# 3. 运行脚本
./terminal-demo.sh

# 4. 选择选项 7（运行完整演示）
# 5. 演示会自动运行所有功能
# 6. 完成后按 Ctrl+Alt+Shift+R 停止录制
```

### 方法 2: 使用 asciinema（专业终端录制）

```bash
# 安装 asciinema
sudo apt-get install asciinema -y

# 开始录制
asciinema rec movie-ticket-demo.cast

# 运行演示脚本
./terminal-demo.sh

# 选择选项 7 进行完整演示

# 完成后按 Ctrl+D 结束录制

# 播放录制内容
asciinema play movie-ticket-demo.cast

# 上传到 asciinema.org（可选）
asciinema upload movie-ticket-demo.cast
```

### 方法 3: 使用 SimpleScreenRecorder

```bash
# 安装
sudo apt-get install simplescreenrecorder -y

# 运行
simplescreenrecorder

# 在界面中选择录制区域（选择终端窗口）
# 点击开始录制
# 运行演示脚本并选择选项 7
# 完成后停止录制
```

## 📝 功能说明

### 选项 1-5: 单独测试各个服务
- 测试单个微服务的功能
- 显示格式化的 JSON 响应
- 适合详细演示某个服务

### 选项 6: 健康检查
- 检查所有微服务的运行状态
- 显示每个服务是否在线
- 适合开始演示前的验证

### 选项 7: 完整演示（推荐录屏使用）
- 自动依次演示所有 5 个微服务
- 每个服务演示后自动等待 3 秒
- 无需手动操作，适合录制演示视频
- 约需 20-30 秒完成

## 🎨 界面特点

- ✅ 彩色终端输出
- ✅ 美观的边框和分隔线
- ✅ Emoji 图标
- ✅ 自动格式化 JSON
- ✅ 清晰的状态提示

## ⚙️ 配置

如果需要修改 API 地址，编辑脚本中的这一行：

```bash
API_BASE="http://localhost:8090"
```

## 🔧 故障排查

### 问题 1: 脚本无法运行，提示 "未找到命令"

```bash
# 检查文件格式
file terminal-demo.sh

# 如果显示 "CRLF"，需要转换
dos2unix terminal-demo.sh
# 或
sed -i 's/\r$//' terminal-demo.sh

# 确保有执行权限
chmod +x terminal-demo.sh
```

### 问题 2: 颜色不显示

```bash
# 确保终端支持颜色
echo $TERM

# 如果不是 xterm-256color，设置一下
export TERM=xterm-256color
```

### 问题 3: API 请求失败

```bash
# 检查 Docker 服务是否运行
docker ps

# 检查端口是否可访问
curl http://localhost:8090/api/users

# 查看服务日志
docker logs gateway-service
```

### 问题 4: JSON 格式化失败

```bash
# 检查是否安装了 Python3
python3 --version

# 如果没有，安装
sudo apt-get install python3 -y
```

## 📊 演示流程建议

### 完整演示流程（约 2 分钟）

1. **开场** (10秒)
   - 显示菜单界面
   - 说明系统功能

2. **健康检查** (10秒)
   - 选择选项 6
   - 展示所有服务在线状态

3. **完整演示** (30秒)
   - 选择选项 7
   - 自动演示所有功能

4. **单独演示**（可选，每个 10-15 秒）
   - 选择选项 1-5
   - 详细展示某个服务

5. **结束** (5秒)
   - 选择选项 0 退出
   - 显示再见信息

## 💡 提示

- 录屏前先运行一次脚本，确保所有服务正常
- 使用全屏终端，字体调大一些便于观看
- 选项 7 的完整演示最适合录制视频
- 可以先练习几次再正式录制

## 🎬 示例录制命令

```bash
# 一键运行演示（适合快速录制）
cd ~/桌面
./terminal-demo.sh <<EOF
7
EOF
```

这会自动选择选项 7 运行完整演示，无需手动输入。
