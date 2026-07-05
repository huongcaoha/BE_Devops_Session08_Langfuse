//package org.example.session08.config;
//
//
//import com.github.langfuse.Langfuse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class LangfuseConfig {
//
//    @Value("${langfuse.public-key}")
//    private String publicKey;
//
//    @Value("${langfuse.secret-key}")
//    private String secretKey;
//
//    @Value("${langfuse.host}")
//    private String host;
//
//    @Bean
//    public Langfuse langfuseClient() {
//        return new Langfuse(publicKey, secretKey, host);
//    }
//}
