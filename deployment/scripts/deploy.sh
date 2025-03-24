#!/bin/bash
# AWS SSM에서 최신 MONGO_URI 가져오기
MONGO_URI=$(aws ssm get-parameter --name "/my-app/documentdb-uri" --with-decryption --query "Parameter.Value" --output text)

# MONGO_URI가 올바르게 가져와졌는지 확인
if [ -z "$MONGO_URI" ]; then
  echo "Error: MONGO_URI 값을 가져오지 못했습니다."
  exit 1
fi

# 기존 컨테이너 중지 및 삭제 (없으면 무시)
docker stop my-backend-app || true
docker rm my-backend-app || true

# 기존 Docker 이미지 삭제 (삭제할 이미지가 있을 경우에만)
IMAGE_IDS=$(docker images -q)
if [ -n "$IMAGE_IDS" ]; then
  docker rmi $IMAGE_IDS || true
fi

# 최신 Docker 이미지 가져오기 및 컨테이너 실행(latest 태그 유지)
docker pull ddongu/my-backend-app:latest
docker run -d --name my-backend-app -p 8080:8080 -e MONGO_URI="${MONGO_URI}" ddongu/my-backend-app:latest
