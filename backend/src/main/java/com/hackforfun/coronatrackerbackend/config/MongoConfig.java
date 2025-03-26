package com.hackforfun.coronatrackerbackend.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Configuration
public class MongoConfig {

    private static final Logger logger = Logger.getLogger(MongoConfig.class.getName());
    private static final String MONGO_URI = System.getenv("MONGO_URI");
    private static final String CERT_URL = "https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem";
    private static final String CERT_PATH = "/app/global-bundle.pem";

    @Bean
    public MongoTemplate mongoTemplate() {
        try {
            logger.info("🔹 MongoDB 설정 시작...");

            // 인증서 다운로드 (컨테이너 내 경로에 저장)
            logger.info("📥 인증서 다운로드 시작: " + CERT_URL);
            downloadCertificate(CERT_URL, CERT_PATH);
            logger.info("✅ 인증서 다운로드 완료: " + CERT_PATH);

            // 🔹 MONGO_URI를 URL 디코딩
            String decodedUri = URLDecoder.decode(MONGO_URI, StandardCharsets.UTF_8.toString());
            logger.info("🔗 디코딩된 MongoDB URI: " + decodedUri);

            // MongoDB 연결 문자열 생성
            String finalUri = decodedUri + "&tlsCAFile=" + CERT_PATH;
            logger.info("🔗 최종 MongoDB 연결 URI: " + finalUri);

            ConnectionString connectionString = new ConnectionString(finalUri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(settings), "coronatracker");
            logger.info("🚀 MongoDB 연결 성공!");

            return mongoTemplate;
        } catch (Exception e) {
            logger.severe("❌ MongoDB 연결 실패: " + e.getMessage());
            throw new RuntimeException("MongoDB 연결 실패: " + e.getMessage(), e);
        }
    }

    private void downloadCertificate(String certUrl, String outputPath) throws Exception {
        File certFile = new File(outputPath);
        if (!certFile.exists()) {
            try (InputStream in = new URL(certUrl).openStream();
                 FileOutputStream out = new FileOutputStream(certFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            certFile.setReadable(true, false);
            logger.info("✅ 인증서 저장 완료: " + outputPath);
        } else {
            logger.info("⚡ 이미 인증서가 존재함, 다운로드 생략: " + outputPath);
        }
    }
}
