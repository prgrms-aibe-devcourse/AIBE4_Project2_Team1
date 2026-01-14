package websocket.dto.view;

import java.time.LocalDateTime;

public record ScheduleDto(
        Long scheduleId,
        Long teacherId,
        String teacherName,
        String subject,
        Long price,
        LocalDateTime startDateTime
) {
}
