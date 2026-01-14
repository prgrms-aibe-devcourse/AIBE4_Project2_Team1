package websocket.dto.api.request;

import java.time.LocalDateTime;

public record ScheduleRequest(
        Long lessonId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
