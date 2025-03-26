package com.hackforfun.coronatrackerbackend.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.logging.Logger;

@Configuration
public class MongoConfig {
    private static final Logger logger = Logger.getLogger(MongoConfig.class.getName());
    private static final String MONGO_URI = System.getenv("MONGO_URI");

    @Bean
    public MongoTemplate mongoTemplate() {
        try {
            logger.info("🔹 MongoDB 설정 시작...");

            if (MONGO_URI == null || MONGO_URI.isEmpty()) {
                throw new RuntimeException("❌ 환경 변수 MONGO_URI가 설정되지 않았습니다.");
            }

            // TLS 인증서를 직접 참조하는 방식 (Keystore 사용하지 않음)
            ConnectionString connectionString = new ConnectionString(MONGO_URI);
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
}
