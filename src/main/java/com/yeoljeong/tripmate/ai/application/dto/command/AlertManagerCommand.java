package com.yeoljeong.tripmate.ai.application.dto.command;

import com.yeoljeong.tripmate.ai.domain.alert.Alert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertManagerCommand {

    private final String receiver;
    private final String status;
    private final List<AlertCommand> alerts;
    private final Map<String, String> groupLabels;
    private final Map<String, String> commonLabels;
    private final Map<String, String> commonAnnotations;
    private final String externalUrl;

    public List<Alert> toAlerts() {
        if (alerts == null || alerts.isEmpty()) {
            return List.of();
        }

        return alerts.stream()
                .map(AlertCommand::toAlert)
                .toList();
    }

    @Getter
    @Builder
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AlertCommand {

        private final Map<String, String> labels;
        private final Map<String, String> annotations;
        private final String status;
        private final String generatorUrl;
        private final String fingerprint;
        private final OffsetDateTime startsAt;
        private final OffsetDateTime endsAt;

        public Alert toAlert() {
            return Alert.of(labels, annotations, status,
                    generatorUrl, fingerprint, startsAt, endsAt);
        }
    }
}
