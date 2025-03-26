// package com.hackforfun.coronatrackerbackend.config;

// import com.mongodb.ConnectionString;
// import com.mongodb.MongoClientSettings;
// import com.mongodb.client.MongoClients;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.mongodb.core.MongoTemplate;

// import java.io.File;
// import java.util.logging.Logger;

// @Configuration
// public class MongoConfig {
//     private static final Logger logger = Logger.getLogger(MongoConfig.class.getName());

//     // 환경 변수에서 MongoDB URI 및 TLS 인증서 경로 가져오기
//     private static final String MONGO_URI = System.getenv("MONGO_URI");
//     private static final String TLS_CERT_PATH = "/tmp/global-bundle.pem";  // TLS 인증서 경로

//     @Bean
//     public MongoTemplate mongoTemplate() {
//         try {
//             logger.info("🔹 MongoDB 설정 시작...");

//             // ✅ MONGO_URI가 설정되지 않은 경우 예외 발생
//             if (MONGO_URI == null || MONGO_URI.isEmpty()) {
//                 throw new RuntimeException("❌ 환경 변수 MONGO_URI가 설정되지 않았습니다.");
//             }

//             // ✅ 인증서 파일 존재 여부 확인
//             File certFile = new File(TLS_CERT_PATH);
//             if (!certFile.exists()) {
//                 throw new RuntimeException("❌ 인증서 파일을 찾을 수 없습니다: " + TLS_CERT_PATH);
//             }

//             // ✅ MongoDB URI에 TLS 설정 추가 (기존 URI에 '?'가 있는지 확인하여 '&' 또는 '?' 사용)
//             String mongoUriWithTLS = MONGO_URI.contains("?") 
//                 ? MONGO_URI + "&tlsCAFile=" + TLS_CERT_PATH
//                 : MONGO_URI + "?tlsCAFile=" + TLS_CERT_PATH;

//             logger.info("📡 MongoDB 연결 URI: " + mongoUriWithTLS);

//             // ✅ MongoDB 연결 설정
//             ConnectionString connectionString = new ConnectionString(mongoUriWithTLS);
//             MongoClientSettings settings = MongoClientSettings.builder()
//                     .applyConnectionString(connectionString)
//                     .build();

//             // ✅ MongoDB 클라이언트 생성 및 연결 확인
//             try {
//                 MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(settings), "coronatracker");
//                 logger.info("🚀 MongoDB 연결 성공!");
//                 return mongoTemplate;
//             } catch (Exception e) {
//                 logger.severe("❌ MongoDB 연결 중 오류 발생: " + e.getMessage());
//                 throw new RuntimeException("MongoDB 연결 중 오류 발생", e);
//             }

//         } catch (Exception e) {
//             logger.severe("❌ MongoDB 설정 실패: " + e.getMessage());
//             throw new RuntimeException("MongoDB 설정 실패", e);
//         }
//     }
// }
