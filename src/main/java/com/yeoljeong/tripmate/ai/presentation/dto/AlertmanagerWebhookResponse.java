package com.yeoljeong.tripmate.ai.presentation.dto;

import com.yeoljeong.tripmate.ai.application.dto.result.AlertManagerResult;

public record AlertmanagerWebhookResponse(
        int receivedCount,
        int processedCount,
        int skippedCount,
        String message
) {

    public static AlertmanagerWebhookResponse from(AlertManagerResult result) {
        return new AlertmanagerWebhookResponse(
                result.receivedCount(),
                result.processedCount(),
                result.skippedCount(),
                "Alertmanager webhook processed"
        );
    }
}
