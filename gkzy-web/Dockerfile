# ===== Stage 1: Node 编译 =====
FROM node:20-alpine AS build
WORKDIR /build

# 先复制 package 文件（缓存层）
COPY package.json package-lock.json ./
RUN npm ci

# 复制源码
COPY . .

# 生产构建
RUN npm run build

# ===== Stage 2: Nginx 服务 =====
FROM nginx:1.27-alpine AS runtime

# 覆盖默认 nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 复制打包好的静态文件
COPY --from=build /build/dist /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://localhost:80/ || exit 1

CMD ["nginx", "-g", "daemon off;"]
