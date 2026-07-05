package org.example.session08.controller;

import org.example.session08.model.dto.request.ChatRequest;
import org.example.session08.service.RagChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    @Autowired
    private RagChatService ragChatService;

    @PostMapping
    public ResponseEntity<String> chatWithUser(@RequestBody ChatRequest request) {
        return new ResponseEntity<>(ragChatService.askQuestion(request.getMessage()), HttpStatus.OK);
    }
}
