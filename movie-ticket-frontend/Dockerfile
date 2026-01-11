# 使用 nginx 作为基础镜像
FROM nginx:alpine

# 设置维护者信息
LABEL maintainer="Movie Ticket System"

# 删除 nginx 默认配置
RUN rm -rf /usr/share/nginx/html/*
RUN rm /etc/nginx/conf.d/default.conf

# 复制 nginx 配置文件
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 复制前端文件到 nginx 目录
COPY . /usr/share/nginx/html/

# 暴露 80 端口
EXPOSE 80

# 启动 nginx
CMD ["nginx", "-g", "daemon off;"]
