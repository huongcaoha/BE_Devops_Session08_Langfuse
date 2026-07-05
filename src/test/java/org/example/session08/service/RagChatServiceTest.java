package org.example.session08.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RagChatServiceTest {

    @Mock
    private VectorStore vectorStore;

    @Mock
    private ChatClient.Builder chatClientBuilder;

    private RagChatService ragChatService;

    @BeforeEach
    void setUp() {
        // Deep stub the ChatClient
        ChatClient chatClientMock = Mockito.mock(ChatClient.class, Mockito.RETURNS_DEEP_STUBS);
        
        when(chatClientBuilder.defaultSystem(anyString())).thenReturn(chatClientBuilder);
        when(chatClientBuilder.build()).thenReturn(chatClientMock);

        // Define the behavior of the fluid API
        when(chatClientMock.prompt()
                .user(anyString())
                .advisors(ArgumentMatchers.any(QuestionAnswerAdvisor.class))
                .call()
                .content())
                .thenReturn("Mocked Answer");

        ragChatService = new RagChatService(chatClientBuilder, vectorStore);
    }

    @Test
    void testAskQuestion() {
        String question = "What is the policy?";
        String response = ragChatService.askQuestion(question);

        assertEquals("Mocked Answer", response);
    }
}
