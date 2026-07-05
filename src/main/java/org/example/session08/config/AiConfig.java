package org.example.session08.config;

//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.embedding.EmbeddingModel;
//import org.springframework.ai.ollama.OllamaChatModel;
//import org.springframework.ai.ollama.OllamaEmbeddingModel;
//import org.springframework.ai.ollama.api.OllamaApi;
//import org.springframework.ai.ollama.api.OllamaOptions;
//import org.springframework.ai.vectorstore.PgVectorStore;
//import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.client.RestClientCustomizer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
//import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AiConfig {

    @Bean
    public RestClientCustomizer geminiUsageFixCustomizer() {
        return restClientBuilder -> {
            restClientBuilder.requestInterceptor(new ClientHttpRequestInterceptor() {
                @Override
                public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                    ClientHttpResponse response = execution.execute(request, body);
                    if (request.getURI().toString().contains("v1beta/openai") && request.getURI().toString().contains("embeddings")) {
                        byte[] responseBodyBytes = StreamUtils.copyToByteArray(response.getBody());
                        String responseBody = new String(responseBodyBytes, StandardCharsets.UTF_8);
                        
                        // Nếu response chưa có usage, ta chèn một object usage giả vào
                        if (!responseBody.contains("\"usage\"")) {
                            if (responseBody.endsWith("}\n")) {
                                responseBody = responseBody.substring(0, responseBody.lastIndexOf("}"));
                            } else if (responseBody.endsWith("}")) {
                                responseBody = responseBody.substring(0, responseBody.length() - 1);
                            }
                            responseBody += ", \"usage\": {\"prompt_tokens\": 0, \"total_tokens\": 0}}";
                        }
                        
                        byte[] newResponseBodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);
                        return new ClientHttpResponse() {
                            @Override
                            public org.springframework.http.HttpStatusCode getStatusCode() throws IOException {
                                return response.getStatusCode();
                            }

                            @Override
                            public String getStatusText() throws IOException {
                                return response.getStatusText();
                            }
                            @Override
                            public void close() {
                                response.close();
                            }
                            @Override
                            public java.io.InputStream getBody() throws IOException {
                                return new java.io.ByteArrayInputStream(newResponseBodyBytes);
                            }
                            @Override
                            public org.springframework.http.HttpHeaders getHeaders() {
                                return response.getHeaders();
                            }
                        };
                    }
                    return response;
                }
            });
        };
    }

//    @Bean
//    public EmbeddingModel embeddingModel() {
//        // Trỏ tới URL của Ollama server
//        var ollamaApi = new OllamaApi("http://localhost:11434");
//        // Lưu ý cần tải model này trên ollama bằng cách mở cmd là gõ : ollama pull nomic-embed-text
//        return new OllamaEmbeddingModel(ollamaApi,
//                OllamaOptions.create()
//                        .withModel("nomic-embed-text")); // Đảm bảo bạn đã tải model này (ollama pull nomic-embed-text)
//    }
//
//    @Bean
//    public ChatModel chatModel() {
//        var ollamaApi = new OllamaApi("http://localhost:11434");
//        return new OllamaChatModel(ollamaApi,
//                OllamaOptions.create()
//                        .withModel("qwen2.5:7b") // Ép buộc dùng đúng model này
//                        .withTemperature(0.2f));
//    }
//
////    @Bean
////    public VectorStore vectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
////        // Khởi tạo PgVectorStore với database connection và model tạo vector
////        return new PgVectorStore(jdbcTemplate, embeddingModel);
////    }
}