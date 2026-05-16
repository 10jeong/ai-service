package com.yeoljeong.tripmate.ai.infrastructure.llm.dto;

import java.util.List;

public record LlmChatResponse(
        List<Choice> choices
) {

    public record Choice(
            Message message
    ) {
    }

    public record Message(
            String role,
            String content
    ) {
    }

    public String firstContent() {
        if (choices == null || choices.isEmpty()) {
            return "LLM 분석 결과가 비어 있습니다.";
        }

        Message message = choices.get(0).message();

        if (message == null || message.content() == null) {
            return "LLM 분석 결과가 비어 있습니다.";
        }

        return message.content();
    }
}
