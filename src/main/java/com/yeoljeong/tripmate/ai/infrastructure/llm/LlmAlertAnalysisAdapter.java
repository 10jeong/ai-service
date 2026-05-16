package com.yeoljeong.tripmate.ai.infrastructure.llm;

import com.yeoljeong.tripmate.ai.application.port.AlertAnalysisPort;
import com.yeoljeong.tripmate.ai.domain.alert.Alert;
import com.yeoljeong.tripmate.ai.domain.alert.PrometheusMetricContext;
import com.yeoljeong.tripmate.ai.infrastructure.llm.dto.LlmChatRequest;
import com.yeoljeong.tripmate.ai.infrastructure.llm.dto.LlmChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class LlmAlertAnalysisAdapter implements AlertAnalysisPort {

    private final RestClient.Builder restClientBuilder;

    @Value("${external.llm.base-url}")
    private String llmBaseUrl;

    @Value("${external.llm.api-key}")
    private String apiKey;

    @Value("${external.llm.model}")
    private String model;

    @Override
    public String analyze(Alert alert, PrometheusMetricContext metricContext) {
        RestClient restClient = restClientBuilder
                .baseUrl(llmBaseUrl)
                .build();

        LlmChatRequest request = LlmChatRequest.of(
                model,
                systemPrompt(),
                userPrompt(alert, metricContext)
        );

        LlmChatResponse response = restClient.post()
                .uri("/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .body(request)
                .retrieve()
                .body(LlmChatResponse.class);

        if (response == null) {
            return "LLM 응답이 비어 있습니다.";
        }

        return response.firstContent();
    }

    private String systemPrompt() {
        return """
                당신은 MSA 운영 환경의 장애 알림을 분석하는 SRE assistant입니다.
                Alertmanager 알림과 Prometheus 조회 결과를 바탕으로 원인 후보와 대응 방법을 간단히 정리하세요.
                답변은 한국어로 작성하세요.
                너무 길게 쓰지 말고 Slack 알림에 적합하게 요약하세요.
                """;
    }

    private String userPrompt(Alert alert, PrometheusMetricContext metricContext) {
        return """
                [Alert 정보]
                제목: %s
                서비스명: %s
                상태: %s
                심각도: %s
                요약: %s
                설명: %s
                
                [Prometheus 조회 결과]
                %s
                
                위 정보를 바탕으로 다음 형식으로 분석하세요.
                
                1. 장애 요약
                2. 원인 후보
                3. 확인할 지표
                4. 우선 대응 방법
                """.formatted(
                alert.title(),
                alert.getServiceName(),
                alert.getStatus().displayName(),
                alert.getSeverity().displayName(),
                alert.getSummary(),
                alert.getDescription(),
                metricContext.toPromptText()
        );
    }
}
