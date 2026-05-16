package com.yeoljeong.tripmate.ai.domain.alert;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// Prometheus에서 조회한 값을 담는 도메인 객체

@Getter
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PrometheusMetricContext {

    private final String serviceName;
    private final String errorRate;
    private final String requestRate;
    private final String cpuUsage;
    private final String memoryUsage;
    private final boolean available;
    private final String message;

    public static PrometheusMetricContext available(
            String serviceName,
            String errorRate,
            String requestRate,
            String cpuUsage,
            String memoryUsage
    ) {
        return new PrometheusMetricContext(
                serviceName,
                defaultValue(errorRate),
                defaultValue(requestRate),
                defaultValue(cpuUsage),
                defaultValue(memoryUsage),
                true,
                "Prometheus 메트릭 조회 성공"
        );
    }

    public static PrometheusMetricContext unavailable(String serviceName, String message) {
        return new PrometheusMetricContext(
                serviceName,
                "N/A",
                "N/A",
                "N/A",
                "N/A",
                false,
                message
        );
    }

    public String toPromptText() {
        if (!available) {
            return """
                    Prometheus 추가 메트릭 조회 실패
                    - 서비스: %s
                    - 사유: %s
                    """.formatted(serviceName, message);
        }

        return """
                Prometheus 추가 메트릭
                - 서비스: %s
                - 최근 에러율: %s
                - 최근 요청량: %s
                - CPU 사용률: %s
                - 메모리 사용량: %s
                """.formatted(
                serviceName,
                errorRate,
                requestRate,
                cpuUsage,
                memoryUsage
        );
    }

    private static String defaultValue(String value) {
        if (value == null || value.isBlank()) {
            return "N/A";
        }

        return value;
    }

}
