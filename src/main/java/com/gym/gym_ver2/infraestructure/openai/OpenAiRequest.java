package com.gym.gym_ver2.infraestructure.openai;

import org.apache.logging.log4j.message.Message;

import java.util.List;

public class OpenAiRequest {
    private String model = "gpt-3.5-turbo";
    private String prompt;
    private int maxTokens = 1000;
    private double temperature = 0.7;
    private String responseFormat = "text";
    private String userId;
    private String language = "es"; // Default to Spanish

    private List<Message> messages;

    public OpenAiRequest(List<Message> messages) {
        this.messages = messages;
    }

    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        // getters y setters
    }


}
