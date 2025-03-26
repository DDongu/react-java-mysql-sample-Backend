package com.hackforfun.coronatrackerbackend.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
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

            // 1️⃣ 인증서 다운로드
            logger.info("📥 인증서 다운로드 시작: " + CERT_URL);
            downloadCertificate(CERT_URL, CERT_PATH);
            logger.info("✅ 인증서 다운로드 완료: " + CERT_PATH);

            // 2️⃣ 인증서를 Java Keystore에 등록
            logger.info("🔑 인증서 Keystore 등록 시작...");
            addCertificateToKeystore(CERT_PATH);
            logger.info("✅ 인증서 Keystore 등록 완료!");

            // 3️⃣ MongoDB 연결 설정
            logger.info("🔗 MongoDB 연결 URI: " + MONGO_URI);
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

    /**
     * 🔹 인증서 다운로드
     */
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

    /**
     * 🔑 인증서를 Java Keystore에 추가
     */
    private void addCertificateToKeystore(String certPath) throws Exception {
        File certFile = new File(certPath);
        if (!certFile.exists()) {
            throw new FileNotFoundException("❌ 인증서 파일을 찾을 수 없습니다: " + certPath);
        }

        try (InputStream certInput = new FileInputStream(certFile)) {
            // 인증서 객체 생성
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            Certificate cert = certFactory.generateCertificate(certInput);

            // Java Keystore 가져오기
            File keystoreFile = new File(System.getProperty("java.home") + "/lib/security/cacerts");
            if (!keystoreFile.exists()) {
                throw new FileNotFoundException("❌ Keystore 파일을 찾을 수 없습니다: " + keystoreFile.getAbsolutePath());
            }

            try (InputStream keystoreInput = new FileInputStream(keystoreFile)) {
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(keystoreInput, "changeit".toCharArray());

                // 이미 등록된 인증서인지 확인
                if (keystore.getCertificate("rds-cert") == null) {
                    keystore.setCertificateEntry("rds-cert", cert);

                    try (FileOutputStream keystoreOutput = new FileOutputStream(keystoreFile)) {
                        keystore.store(keystoreOutput, "changeit".toCharArray());
                        logger.info("✅ 인증서가 Java Keystore에 추가됨: rds-cert");
                    }
                } else {
                    logger.info("⚡ 인증서가 이미 Java Keystore에 등록되어 있음: rds-cert");
                }
            }
        }
    }
}
