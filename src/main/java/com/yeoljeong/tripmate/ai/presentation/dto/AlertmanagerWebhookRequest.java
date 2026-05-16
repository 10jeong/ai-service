package com.yeoljeong.tripmate.ai.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yeoljeong.tripmate.ai.application.dto.command.AlertManagerCommand;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record AlertmanagerWebhookRequest(
        String receiver,
        String status,
        List<AlertRequest> alerts,
        Map<String, String> groupLabels,
        Map<String, String> commonLabels,
        Map<String, String> commonAnnotations,

        // 'Url'이 아닌, 'URL'로 들어오는 값을 받음
        @JsonProperty("externalURL")
        String externalUrl
) {

    public AlertManagerCommand toCommand() {
        return AlertManagerCommand.builder()
                .receiver(receiver)
                .status(status)
                .alerts(toAlertCommands())
                .groupLabels(groupLabels)
                .commonLabels(commonLabels)
                .commonAnnotations(commonAnnotations)
                .externalUrl(externalUrl)
                .build();
    }

    private List<AlertManagerCommand.AlertCommand> toAlertCommands() {
        if (alerts == null || alerts.isEmpty()) {
            return List.of();
        }

        return alerts.stream()
                .map(AlertRequest::toCommand)
                .toList();
    }

    public record AlertRequest(
            String status,
            Map<String, String> labels,
            Map<String, String> annotations,
            OffsetDateTime startsAt,
            OffsetDateTime endsAt,

            @JsonProperty("generatorURL")
            String generatorUrl,

            String fingerprint
    ) {

        private AlertManagerCommand.AlertCommand toCommand() {
            return AlertManagerCommand.AlertCommand.builder()
                    .status(status)
                    .labels(labels)
                    .annotations(annotations)
                    .startsAt(startsAt)
                    .endsAt(endsAt)
                    .generatorUrl(generatorUrl)
                    .fingerprint(fingerprint)
                    .build();
        }
    }
}
