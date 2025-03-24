#!/bin/bash

# 중지된 컨테이너 제거
docker stop backend-container || true
docker rm backend-container || true

# 최신 Docker 이미지 pull
docker pull $DOCKER_USERNAME/my-backend-app:latest

# 새로운 컨테이너 실행
docker run -d --name backend-container -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://database-host:3306/coronatracker \
  -e SPRING_DATASOURCE_USERNAME=dbuser \
  -e SPRING_DATASOURCE_PASSWORD=dbpassword \
  $DOCKER_USERNAME/my-backend-app:latest
