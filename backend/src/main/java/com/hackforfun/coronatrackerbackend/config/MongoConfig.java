package com.hackforfun.coronatrackerbackend.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

@Configuration
public class MongoConfig {

    private static final String MONGO_URI = System.getenv("MONGO_URI");
    private static final String CERT_URL = "https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem";
    private static final String CERT_PATH = "/app/global-bundle.pem";

    @Bean
    public MongoTemplate mongoTemplate() {
        try {
            // 인증서 다운로드 (컨테이너 내 경로에 저장)
            downloadCertificate(CERT_URL, CERT_PATH);

            // MongoDB 연결 설정
            ConnectionString connectionString = new ConnectionString(
                MONGO_URI + "&tlsCAFile=" + CERT_PATH
            );

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            return new MongoTemplate(MongoClients.create(settings), "coronatracker");
        } catch (Exception e) {
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
        }
    }
}
