package com.yeoljeong.tripmate.ai.infrastructure.prometheus;

import com.yeoljeong.tripmate.ai.application.port.PrometheusMetricQueryPort;
import com.yeoljeong.tripmate.ai.domain.alert.Alert;
import com.yeoljeong.tripmate.ai.domain.alert.PrometheusMetricContext;
import com.yeoljeong.tripmate.ai.infrastructure.prometheus.dto.PrometheusQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class PrometheusMetricQueryAdapter implements PrometheusMetricQueryPort {

    private final RestClient.Builder restClientBuilder;

    @Value("${external.prometheus.base-url}")
    private String prometheusBaseUrl;

    @Override
    public PrometheusMetricContext query(Alert alert) {
        RestClient restClient = restClientBuilder
                .baseUrl(prometheusBaseUrl)
                .build();

        // 현재는 메트릭별로 Prometheus를 개별 조회한다.
        // Alertmanager Webhook 발생 시에만 실행되므로 초기 구현에서는 단순성을 우선한다.
        // 추후 알림 빈도가 많아질 경우 PromQL 통합 조회 방식으로 최적화할 수 있다.
        String serviceName = alert.getServiceName();
        String errorRate = queryValue(restClient, buildErrorRateQuery(serviceName));
        String requestRate = queryValue(restClient, buildRequestRateQuery(serviceName));
        String cpuUsage = queryValue(restClient, buildCpuUsageQuery(serviceName));
        String memoryUsage = queryValue(restClient, buildMemoryUsageQuery(serviceName));

        return PrometheusMetricContext.available(serviceName, errorRate, requestRate, cpuUsage, memoryUsage);
    }

    private String queryValue(RestClient restClient, String query) {
        PrometheusQueryResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/query")
                        .queryParam("query", "{query}")
                        .build(query))
                .retrieve()
                .body(PrometheusQueryResponse.class);

        return extractValue(response);
    }

    private String extractValue(PrometheusQueryResponse response) {
        if (response == null || response.data() == null
                || response.data().result() == null
                || response.data().result().isEmpty()) {
            return "N/A";
        }

        PrometheusQueryResponse.Result result = response.data().result().get(0);

        if (result.value() == null || result.value().size() < 2) {
            return "N/A";
        }

        Object value = result.value().get(1);

        if (value == null) {
            return "N/A";
        }

        return String.valueOf(value);
    }

    private String buildErrorRateQuery(String serviceName) {
        return """
                sum(rate(http_server_requests_seconds_count{application="%s", status=~"5.."}[5m]))
                /
                sum(rate(http_server_requests_seconds_count{application="%s"}[5m]))
                """.formatted(serviceName, serviceName);
    }

    private String buildRequestRateQuery(String serviceName) {
        return """
                sum(rate(http_server_requests_seconds_count{application="%s"}[5m]))
                """.formatted(serviceName);
    }

    private String buildCpuUsageQuery(String serviceName) {
        return """
                process_cpu_usage{application="%s"}
                """.formatted(serviceName);
    }

    private String buildMemoryUsageQuery(String serviceName) {
        return """
                sum(jvm_memory_used_bytes{application="%s"})
                """.formatted(serviceName);
    }
}
