package com.yeoljeong.tripmate.ai.application.dto.command;

import com.yeoljeong.tripmate.ai.domain.alert.Alert;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record AlertManagerCommand(
        String receiver,
        String status,
        List<AlertCommand> alerts,
        Map<String, String> groupLabels,
        Map<String, String> commonLabels,
        Map<String, String> commonAnnotations,
        String externalUrl
) {

    public List<Alert> toAlerts() {
        if (alerts == null || alerts.isEmpty()) {
            return List.of();
        }

        return alerts.stream()
                .map(AlertCommand::toAlert)
                .toList();
    }

    public record AlertCommand(
            Map<String, String> labels,
            Map<String, String> annotations,
            String status,
            String generatorUrl,
            String fingerprint,
            OffsetDateTime startsAt,
            OffsetDateTime endsAt
    ) {

        public Alert toAlert() {
            return Alert.of(
                    labels,
                    annotations,
                    status,
                    generatorUrl,
                    fingerprint,
                    startsAt,
                    endsAt
            );
        }
    }
}
