package com.yeoljeong.tripmate.ai.application.port;

import com.yeoljeong.tripmate.ai.domain.alert.Alert;
import com.yeoljeong.tripmate.ai.domain.alert.PrometheusMetricContext;

public interface AlertAnalysisPort {

    // Alert와 Prometheus 조회 결과를 기반으로 LLM 분석 결과 생성
    String analyze(Alert alert, PrometheusMetricContext metricContext);
}
