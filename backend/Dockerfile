# 1. Java 17 JDK 기반 이미지 사용
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. DocumentDB 글로벌 인증서 다운로드 (RDS 인증서)
RUN apt-get update && apt-get install -y wget && \
    wget https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem -O /app/global-bundle.pem && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# 4. CI에서 미리 빌드된 .jar 파일을 복사
COPY target/*.jar app.jar

# 5. JAR 파일 실행 (환경 변수로 MONGO_URI 전달)
CMD ["java", "-jar", "app.jar"]
