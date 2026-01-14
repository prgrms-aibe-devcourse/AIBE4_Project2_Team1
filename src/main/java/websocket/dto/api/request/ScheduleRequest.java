package websocket.dto.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ScheduleRequest(
        Long lessonId,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime startTime,
        Integer duration
) {
}
