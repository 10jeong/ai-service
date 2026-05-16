package com.yeoljeong.tripmate.ai.presentation.controller;

import com.yeoljeong.tripmate.ai.application.dto.result.AlertManagerResult;
import com.yeoljeong.tripmate.ai.application.service.AlertProcessingService;
import com.yeoljeong.tripmate.ai.presentation.dto.AlertmanagerWebhookRequest;
import com.yeoljeong.tripmate.ai.presentation.dto.AlertmanagerWebhookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertmanagerWebhookController {

    private final AlertProcessingService alertProcessingService;

    @PostMapping("/alertmanager")
    public ResponseEntity<AlertmanagerWebhookResponse> handleAlertmanagerWebhook(
            @RequestBody AlertmanagerWebhookRequest request
    ) {
        AlertManagerResult result = alertProcessingService.handle(request.toCommand());

        return ResponseEntity.ok(AlertmanagerWebhookResponse.from(result));
    }
}
