package org.example.session08.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class RagChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagChatService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;

        this.chatClient = builder
                .defaultSystem("""
                    Bạn là trợ lý thông thái của nhà trường.
                    KỶ LUẬT THÉP BẮT BUỘC TUÂN THỦ:
                    1. CHỈ được phép trả lời dựa trên thông tin (Context) được cung cấp dưới đây.
                    2. Nếu trong Context không có thông tin, hãy dũng cảm trả lời: "Dạ, tài liệu hiện tại không đề cập đến vấn đề này".
                    3. Tuyệt đối không tự bịa đặt thông tin (No hallucination).
                    """)
                .build();
    }

    public String askQuestion(String userQuestion) {
        SearchRequest searchRequest = SearchRequest.builder().topK(3).build();

        // Toàn bộ quá trình quét VectorDB và gọi LLM sẽ được Spring ngầm vẽ lên dashboard Langfuse
        return this.chatClient.prompt()
                .user(userQuestion)
                .advisors(new QuestionAnswerAdvisor(this.vectorStore, searchRequest))
                .call()
                .content();
    }
}