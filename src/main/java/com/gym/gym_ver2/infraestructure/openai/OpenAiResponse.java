package com.gym.gym_ver2.infraestructure.openai;

import java.util.List;

// DTO para la respuesta
public class OpenAiResponse {
    private List<Choice> choices;

    public static class Choice {
        private OpenAiRequest.Message message;

        public OpenAiRequest.Message getMessage() {
            return message;
        }
    }

    public List<Choice> getChoices() {
        return choices;
    }
}

