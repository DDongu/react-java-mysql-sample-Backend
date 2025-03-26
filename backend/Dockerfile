# Java 17 Full 버전 사용
FROM openjdk:17-jdk

# 작업 디렉토리 설정
WORKDIR /app

# CI에서 미리 빌드된 .jar 파일을 복사
COPY target/*.jar app.jar

# JAR 파일 실행
CMD ["java", "-jar", "app.jar"]
