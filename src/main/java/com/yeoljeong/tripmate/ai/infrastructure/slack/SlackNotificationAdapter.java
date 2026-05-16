package com.yeoljeong.tripmate.ai.infrastructure.slack;

import com.yeoljeong.tripmate.ai.application.port.SlackNotificationPort;
import com.yeoljeong.tripmate.ai.infrastructure.slack.dto.SlackMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class SlackNotificationAdapter implements SlackNotificationPort {

    private final RestClient.Builder restClientBuilder;

    @Value("${external.slack.webhook-url}")
    private String slackWebhookUrl;

    @Override
    public void send(String message) {
        RestClient restClient = restClientBuilder.build();

        restClient.post()
                .uri(slackWebhookUrl)
                .body(SlackMessageRequest.of(message))
                .retrieve()
                .toBodilessEntity();
    }
}
