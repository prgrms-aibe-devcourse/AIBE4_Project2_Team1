package websocket.dto.api.response;


import websocket.entity.lesson.Schedule;

import java.time.LocalDateTime;

public record ScheduleResponse(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(schedule.getId(), schedule.getStartTime(), schedule.getEndTime());
    }
}
