package com.yeoljeong.tripmate.ai.domain.service;

import com.yeoljeong.tripmate.ai.domain.alert.Alert;
import com.yeoljeong.tripmate.ai.domain.alert.PrometheusMetricContext;

// Slack 메시지 문자열 생성 정책
public class AlertMessageFormatter {

    public String formatSlackMessage(
            Alert alert,
            PrometheusMetricContext metricContext,
            String aiAnalysis
    ) {
        return """
                %s
                
                *서비스*: `%s`
                *상태*: `%s`
                *심각도*: `%s`
                
                *요약*
                %s
                
                *설명*
                %s
                
                *Prometheus 추가 정보*
                %s
                
                *AI 분석*
                %s
                
                *Prometheus Rule URL*
                %s
                """.formatted(
                alert.title(),
                alert.getServiceName(),
                alert.getStatus().displayName(),
                alert.getSeverity().displayName(),
                alert.getSummary(),
                alert.getDescription(),
                metricContext.toPromptText(),
                defaultAnalysis(aiAnalysis),
                alert.getGeneratorUrl()
        );
    }

    public String formatFallbackMessage(Alert alert, Exception exception) {
        return """
                ⚠️ *[AI-SERVICE ERROR] 알림 처리 실패*
                
                *알림명*: `%s`
                *서비스*: `%s`
                *상태*: `%s`
                *심각도*: `%s`
                *오류 메시지*: `%s`
                """.formatted(
                alert.getAlertName(),
                alert.getServiceName(),
                alert.getStatus().displayName(),
                alert.getSeverity().displayName(),
                exception.getMessage()
        );
    }

    public String formatSimpleMessage(Alert alert) {
        return """
                %s
                
                *서비스*: `%s`
                *상태*: `%s`
                *심각도*: `%s`
                *요약*: %s
                """.formatted(
                alert.title(),
                alert.getServiceName(),
                alert.getStatus().displayName(),
                alert.getSeverity().displayName(),
                alert.getSummary()
        );
    }

    private String defaultAnalysis(String aiAnalysis) {
        if (aiAnalysis == null || aiAnalysis.isBlank()) {
            return "AI 분석 결과가 없습니다. 기본 알림 정보와 Prometheus/Grafana 대시보드를 기준으로 확인하세요.";
        }

        return aiAnalysis;
    }
}
