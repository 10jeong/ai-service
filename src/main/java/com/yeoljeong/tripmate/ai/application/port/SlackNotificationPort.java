package com.yeoljeong.tripmate.ai.application.port;

public interface SlackNotificationPort {

    // Slack으로 메시지 전송
    void send(String message);
}
