package com.yeoljeong.tripmate.ai.infrastructure.slack.dto;

public record SlackMessageRequest(
        String text
) {

    public static SlackMessageRequest of(String text) {
        return new SlackMessageRequest(text);
    }
}
