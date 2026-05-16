package com.yeoljeong.tripmate.ai.domain.enums;

// Alert manager의 label값에 대응되는 ENUM (심각도)
public enum Severity {

    CRITICAL,
    WARNING,
    INFO,
    UNKNOWN;

    public static Severity from(String value) {
        if (value == null || value.isBlank()) {
            return UNKNOWN;
        }

        return switch (value.toLowerCase()) {
            case "critical" -> CRITICAL;
            case "warning" -> WARNING;
            case "info" -> INFO;
            default -> UNKNOWN;
        };
    }

    public boolean isCritical() {
        return this == CRITICAL;
    }

    public boolean isWarning() {
        return this == WARNING;
    }

    public String displayName() {
        return switch (this) {
            case CRITICAL -> "CRITICAL";
            case WARNING -> "WARNING";
            case INFO -> "INFO";
            case UNKNOWN -> "UNKNOWN";
        };
    }

    public String emoji() {
        return switch (this) {
            case CRITICAL -> "🔥";
            case WARNING -> "⚠️";
            case INFO -> "ℹ️";
            case UNKNOWN -> "❔";
        };
    }
}
