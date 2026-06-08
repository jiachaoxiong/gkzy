# ===== Stage 1: Node 编译 Vue 前端 =====
FROM node:20-alpine AS web-builder
WORKDIR /web
COPY gkzy-web/package*.json ./
RUN npm ci --silent
COPY gkzy-web/ ./
RUN npm run build
# 产物在 /web/dist/

# ===== Stage 2: Maven 编译后端（含前端静态文件） =====
FROM maven:3.9-eclipse-temurin-17-alpine AS java-builder
WORKDIR /build

# 先把前端产物放到 static 目录
COPY --from=web-builder /web/dist/ ./gkzy-server/src/main/resources/static/

# 复制 pom（利用缓存）
COPY pom.xml .
COPY gkzy-parent/pom.xml ./gkzy-parent/
COPY gkzy-common/pom.xml ./gkzy-common/
COPY gkzy-auth/pom.xml ./gkzy-auth/
COPY gkzy-college/pom.xml ./gkzy-college/
COPY gkzy-recommend/pom.xml ./gkzy-recommend/
COPY gkzy-application/pom.xml ./gkzy-application/
COPY gkzy-server/pom.xml ./gkzy-server/

RUN mvn dependency:go-offline -B -q || true

# 复制所有源码
COPY gkzy-parent/src ./gkzy-parent/src
COPY gkzy-common/src   ./gkzy-common/src
COPY gkzy-auth/src     ./gkzy-auth/src
COPY gkzy-college/src  ./gkzy-college/src
COPY gkzy-recommend/src ./gkzy-recommend/src
COPY gkzy-application/src ./gkzy-application/src
COPY gkzy-server/src   ./gkzy-server/src

RUN mvn package -DskipTests -B -q

# ===== Stage 3: JRE 运行 =====
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

RUN addgroup -g 1000 gkzy && adduser -u 1000 -G gkzy -D gkzy

COPY --from=java-builder /build/gkzy-server/target/gkzy-server-*.jar app.jar

USER gkzy

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
