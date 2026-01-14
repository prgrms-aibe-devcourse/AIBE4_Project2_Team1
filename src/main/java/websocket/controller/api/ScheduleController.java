package websocket.controller.api;

import websocket.dto.api.response.ApiResponse;
import websocket.dto.api.response.ScheduleSlotResponse;
import websocket.dto.api.response.TeacherScheduleResponse;
import websocket.service.BookingService;
import websocket.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat; // 추가됨
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate; // 반드시 필요
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    // 학생 기능 - 예약 가능 시간 조회
    private final BookingService bookingService;

    // 선생님 기능 - 내 시간표 조회
    private final ScheduleService scheduleService;

    @GetMapping("/availability")
    public ResponseEntity<ApiResponse<List<ScheduleSlotResponse>>> getAvailabilitySchedules(
            @RequestParam("teacherId") Long teacherId, // 명시적으로 파라미터명 지정
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date // 올바른 어노테이션 사용
    ) {
        List<ScheduleSlotResponse> response;

        if (date != null) {
            // 날짜가 전달된 경우 (예: 2026-01-11)
            response = bookingService.getAvailableSchedules(teacherId, date);
        } else {
            // 날짜가 전달되지 않은 경우 전체 조회
            response = bookingService.getAvailableSchedules(teacherId);
        }

        return ResponseEntity.ok(ApiResponse.ok("예약 가능 스케줄 조회 성공", response));
    }

    // ==========================================
    //  [선생님 기능] 내 시간표 조회 (API 명세서 반영)
    // ==========================================
    @GetMapping("/teacher")
    public ResponseEntity<ApiResponse<List<TeacherScheduleResponse>>>getTeacherSchedules(
            @RequestParam("teacherId") Long teacherId
    ){
        List<TeacherScheduleResponse> response = scheduleService.getTeacherSchedules(teacherId);

        return ResponseEntity.ok(ApiResponse.ok("선생님 시간표 조회 성공", response));
    }
}