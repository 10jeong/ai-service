package com.yeoljeong.tripmate.ai.application.dto.result;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertManagerResult {

    private final int receivedCount;
    private final int processedCount;
    private final int skippedCount;

    public static AlertManagerResult of(
            int receivedCount,
            int processedCount,
            int skippedCount
    ) {
        return AlertManagerResult.builder()
                .receivedCount(receivedCount)
                .processedCount(processedCount)
                .skippedCount(skippedCount)
                .build();
    }

    public static AlertManagerResult empty() {
        return AlertManagerResult.builder()
                .receivedCount(0)
                .processedCount(0)
                .skippedCount(0)
                .build();
    }
}
