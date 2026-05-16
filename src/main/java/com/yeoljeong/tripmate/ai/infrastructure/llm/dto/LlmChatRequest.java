package com.yeoljeong.tripmate.ai.infrastructure.llm.dto;

import java.util.List;

public record LlmChatRequest(
        String model,
        List<Message> messages
) {

    public record Message(
            String role,
            String content
    ) {
    }

    public static LlmChatRequest of(
            String model,
            String systemPrompt,
            String userPrompt
    ) {
        return new LlmChatRequest(
                model,
                List.of(
                        new Message("system", systemPrompt),
                        new Message("user", userPrompt)
                )
        );
    }
}
