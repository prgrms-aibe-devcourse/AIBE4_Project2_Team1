package kr.java.pr1mary.controller.api;

import kr.java.pr1mary.dto.api.response.ApiResponse;
import kr.java.pr1mary.dto.api.response.ScheduleSlotResponse;
import kr.java.pr1mary.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final BookingService bookingService;

    // Class-Booking-01 선생님별 예약 가능 일정 조회
    // GET /api/schedules/availability?teacherId=1001
    @GetMapping("/availability")
    public ResponseEntity<ApiResponse<List<ScheduleSlotResponse>>> getAvailabilitySchedules(
            @RequestParam Long teacherId
    ) {
        List<ScheduleSlotResponse> response = bookingService.getAvailableSchedules(teacherId);

        return ResponseEntity.ok(ApiResponse.ok("예약 가능 스케줄 조회 성공", response));
    }
}
