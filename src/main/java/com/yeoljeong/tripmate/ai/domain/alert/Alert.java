package com.yeoljeong.tripmate.ai.domain.alert;

import com.yeoljeong.tripmate.ai.domain.enums.AlertStatus;
import com.yeoljeong.tripmate.ai.domain.enums.Severity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Alert {

    private final String alertName;
    private final String serviceName;
    private final AlertStatus status;
    private final Severity severity;
    private final String summary;
    private final String description;
    private final String generatorUrl;
    private final String fingerprint;
    private final OffsetDateTime startsAt;
    private final OffsetDateTime endsAt;
    private final Map<String, String> labels;
    private final Map<String, String> annotations;

    public static Alert of(
            Map<String, String> labels,
            Map<String, String> annotations,
            String status,
            String generatorUrl,
            String fingerprint,
            OffsetDateTime startsAt,
            OffsetDateTime endsAt
    ) {
        String alertName = getValue(labels, "alertname", "UnknownAlert");

        String serviceName = getValue(labels, "service", null);
        if (serviceName == null) {
            serviceName = getValue(labels, "application", "unknown-service");
        }

        String severityValue = getValue(labels, "severity", "unknown");
        String summary = getValue(annotations, "summary", "요약 정보가 없습니다.");
        String description = getValue(annotations, "description", "상세 설명이 없습니다.");

        return Alert.builder()
                .alertName(alertName)
                .serviceName(serviceName)
                .status(AlertStatus.from(status))
                .severity(Severity.from(severityValue))
                .summary(summary)
                .description(description)
                .generatorUrl(defaultValue(generatorUrl, "N/A"))
                .fingerprint(defaultValue(fingerprint, "N/A"))
                .startsAt(startsAt)
                .endsAt(endsAt)
                .labels(labels == null ? Map.of() : Map.copyOf(labels))
                .annotations(annotations == null ? Map.of() : Map.copyOf(annotations))
                .build();
    }

    public boolean isCritical() {
        return severity.isCritical();
    }

    public boolean isFiring() {
        return status.isFiring();
    }

    public boolean isResolved() {
        return status.isResolved();
    }

    public String title() {
        return "%s [%s] %s".formatted(
                status.emoji(),
                severity.displayName(),
                alertName
        );
    }

    public String label(String key) {
        return labels.get(key);
    }

    public String annotation(String key) {
        return annotations.get(key);
    }

    private static String getValue(Map<String, String> map, String key, String defaultValue) {
        if (map == null) {
            return defaultValue;
        }

        String value = map.get(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value;
    }

    private static String defaultValue(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value;
    }

}
