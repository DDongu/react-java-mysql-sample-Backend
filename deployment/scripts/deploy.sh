#!/bin/bash

# 최신 MONGO_URI 가져오기
MONGO_URI=$(aws secretsmanager get-secret-value --secret-id DocumentDBSecret --query 'SecretString' --output text | jq -r .MONGO_URI)

# 기존 컨테이너 중지 및 삭제
docker stop my-backend-app || true
docker rm my-backend-app || true

# 기존 Docker 이미지 삭제 (디스크 공간 절약)
docker rmi $(docker images -q) || true

# 최신 Docker 이미지 가져오기 (latest 태그 유지)
docker pull $DOCKER_USERNAME/my-backend-app:latest

# 컨테이너 실행 (DocumentDB 연결)
docker run -d --name my-backend-app -p 8080:8080 -e MONGO_URI=$MONGO_URI $DOCKER_USERNAME/my-backend-app:latest
