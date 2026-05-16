package com.yeoljeong.tripmate.ai.application.dto.result;

public record AlertManagerResult(
        int receivedCount,
        int processedCount,
        int skippedCount
) {

    public static AlertManagerResult of(
            int receivedCount,
            int processedCount,
            int skippedCount
    ) {
        return new AlertManagerResult(
                receivedCount,
                processedCount,
                skippedCount
        );
    }

    public static AlertManagerResult empty() {
        return new AlertManagerResult(0, 0, 0);
    }
}
