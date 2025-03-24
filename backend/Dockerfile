# 1. Java 17 JDK 기반 이미지 사용
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. CI에서 미리 빌드된 .jar 파일을 복사
COPY target/*.jar app.jar

# 4. JAR 파일 실행
CMD ["java", "-jar", "app.jar"]
