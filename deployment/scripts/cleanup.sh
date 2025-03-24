#!/bin/bash

# 기존 배포 폴더 삭제
rm -rf /home/ec2-user/backend

# 새 폴더 생성
mkdir -p /home/ec2-user/backend

# 실행 중인 테스트 컨테이너 중지 및 삭제
if [ "$(docker ps -q -f name=backend-app)" ]; then
    echo "Stopping and removing test container..."
    docker stop backend-app
    docker rm backend-app
fi

# 사용하지 않는 Docker 이미지 정리
docker system prune -f
