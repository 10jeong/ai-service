package com.yeoljeong.tripmate.ai.application.service;

import com.yeoljeong.tripmate.ai.application.dto.command.AlertManagerCommand;
import com.yeoljeong.tripmate.ai.application.dto.result.AlertManagerResult;
import com.yeoljeong.tripmate.ai.application.port.AlertAnalysisPort;
import com.yeoljeong.tripmate.ai.application.port.PrometheusMetricQueryPort;
import com.yeoljeong.tripmate.ai.application.port.SlackNotificationPort;
import com.yeoljeong.tripmate.ai.domain.alert.Alert;
import com.yeoljeong.tripmate.ai.domain.alert.PrometheusMetricContext;
import com.yeoljeong.tripmate.ai.domain.service.AlertMessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertProcessingService {

    private final PrometheusMetricQueryPort prometheusMetricQueryPort;
    private final AlertAnalysisPort alertAnalysisPort;
    private final SlackNotificationPort slackNotificationPort;
    private final AlertMessageFormatter alertMessageFormatter;

    public AlertManagerResult handle(AlertManagerCommand command) {
        List<Alert> alerts = command.toAlerts();

        if (alerts.isEmpty()) {
            return AlertManagerResult.empty();
        }

        int processedCount = 0;
        int skippedCount = 0;

        for (Alert alert : alerts) {
            if (shouldSkip(alert)) {
                skippedCount++;
                continue;
            }

            process(alert);
            processedCount++;
        }

        return AlertManagerResult.of(alerts.size(), processedCount, skippedCount);
    }

    // resolved 상태는 알림을 보내지 않음
    private boolean shouldSkip(Alert alert) {
        return alert.isResolved();
    }

    // 추가 처리 후 슬랙 메시지 보냄
    private void process(Alert alert) {
        PrometheusMetricContext metricContext = queryMetricContext(alert);
        String analysisResult = analyzeAlert(alert, metricContext);

        String slackMessage = alertMessageFormatter.formatSlackMessage(alert, metricContext, analysisResult);

        slackNotificationPort.send(slackMessage);
    }

    // prometheus 메트릭 추가 조회
    private PrometheusMetricContext queryMetricContext(Alert alert) {
        try {
            return prometheusMetricQueryPort.query(alert);
        } catch (Exception e) {
            return PrometheusMetricContext.unavailable(alert.getServiceName(),
                    "Prometheus metric query failed: " + e.getMessage()
            );
        }
    }

    // LLM 분석
    private String analyzeAlert(Alert alert, PrometheusMetricContext metricContext) {
        try {
            return alertAnalysisPort.analyze(alert, metricContext);
        } catch (Exception e) {
            return "LLM 분석을 수행하지 못했습니다. 원인: " + e.getMessage();
        }
    }
}
