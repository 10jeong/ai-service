package com.yeoljeong.tripmate.ai.application.port;

import com.yeoljeong.tripmate.ai.domain.alert.Alert;
import com.yeoljeong.tripmate.ai.domain.alert.PrometheusMetricContext;

public interface PrometheusMetricQueryPort {

    // Alert 내용을 기반으로 Prometheus에 추가 메트릭 조회
    PrometheusMetricContext query(Alert alert);
}
