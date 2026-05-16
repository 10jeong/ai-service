package com.yeoljeong.tripmate.ai.domain.enums;

// AlertManager의 알림 상태 문자열에 대응되는 ENUM
public enum AlertStatus {

    FIRING,
    RESOLVED,
    UNKNOWN;

    public static AlertStatus from(String value) {
        if (value == null || value.isBlank()) {
            return UNKNOWN;
        }

        return switch (value.toLowerCase()) {
            case "firing" -> FIRING;
            case "resolved" -> RESOLVED;
            default -> UNKNOWN;
        };
    }

    public boolean isFiring() {
        return this == FIRING;
    }

    public boolean isResolved() {
        return this == RESOLVED;
    }

    public String displayName() {
        return switch (this) {
            case FIRING -> "발생";
            case RESOLVED -> "복구";
            case UNKNOWN -> "알 수 없음";
        };
    }

    public String emoji() {
        return switch (this) {
            case FIRING -> "🚨";
            case RESOLVED -> "✅";
            case UNKNOWN -> "⚠️";
        };
    }
}
