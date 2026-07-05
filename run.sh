#!/bin/bash

# dừng chương trình nếu có bất kỳ lỗi nào xảy ra
set -e

# đảm bảo terminal luôn đứng ở thư mục hiện tịa gọi file docker-compose.yml
cd "$(dirname "$0")"

# dọn dẹp các container cũ nếu có
docker compose down

# chạy build dự án
docker compose build session08-langfuse

# khởi chạy dự án dưới background
docker compose up -d

# kiểm tra docker xem dự án đã chạy thành công hay chưa
docker ps