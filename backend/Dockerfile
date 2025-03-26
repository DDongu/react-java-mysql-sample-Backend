# Java 17 JDK 기반 이미지 사용
FROM openjdk:17-jdk

# 작업 디렉토리 설정
WORKDIR /app

# AWS DocumentDB TLS 인증서 다운로드
RUN apt-get update && apt-get install -y wget && \
    wget https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem -O /tmp/global-bundle.pem && \
    chmod 644 /tmp/global-bundle.pem && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# 애플리케이션 JAR 파일 복사
COPY target/*.jar app.jar

# 환경 변수 설정 (MongoDB에서 인증서를 사용하도록 적용)
ENV TLS_CERT_PATH="/tmp/global-bundle.pem"

# JAR 파일 실행
CMD ["java", "-jar", "app.jar"]
