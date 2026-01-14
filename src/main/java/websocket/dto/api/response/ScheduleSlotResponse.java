package websocket.dto.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import websocket.entity.lesson.Schedule;

import java.time.LocalDateTime;

// 👉 선생님 시간표 탐색/선택
public record ScheduleSlotResponse(
        Long scheduleId,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endTime,

        Boolean isBooked // 마감 여부 - 프론트에서 비활성화 처리용
) {
    public static ScheduleSlotResponse from(Schedule schedule) {
        return new ScheduleSlotResponse(
                schedule.getId(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getIsBooked()
        );
    }
}
