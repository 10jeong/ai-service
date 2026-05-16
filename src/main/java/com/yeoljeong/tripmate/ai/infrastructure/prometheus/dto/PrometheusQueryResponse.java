package com.yeoljeong.tripmate.ai.infrastructure.prometheus.dto;

import java.util.List;
import java.util.Map;

public record PrometheusQueryResponse(
        String status,
        Data data
) {

    public record Data(
            String resultType,
            List<Result> result
    ) {
    }

    public record Result(
            Map<String, String> metric,
            List<Object> value
    ) {
    }
}
