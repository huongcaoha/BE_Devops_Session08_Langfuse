package org.example.session08.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.session08.model.dto.request.ChatRequest;
import org.example.session08.service.RagChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RagChatService ragChatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testChatWithUser_success() throws Exception {
        ChatRequest request = new ChatRequest("Hello?");
        String mockResponse = "This is a mocked response";

        when(ragChatService.askQuestion(anyString())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(mockResponse));
    }
}
