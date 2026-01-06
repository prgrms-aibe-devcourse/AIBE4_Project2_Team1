package kr.java.pr1mary.dto.api.response;

import kr.java.pr1mary.entity.lesson.Schedule;

import java.time.LocalDateTime;

public record ScheduleSlotResponse(
        Long scheduleId,
        LocalDateTime startTime,
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
