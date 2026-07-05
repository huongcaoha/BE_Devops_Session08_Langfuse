# Build dự án thành file .jar
FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app

# copy toàn bộ mã nguồn vào container trước đã
COPY . .
# cấp quyền thực thi và build dự án
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# tạo image trên docker
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# copy file .jar từ giai đoạn builder sang đây  , bị trí làm việc file /app
COPY --from=builder /app/build/libs/*.jar app.jar

# mở port cho dự án
EXPOSE 8080

# lệnh khởi chạy
ENTRYPOINT ["java","-jar","app.jar"]