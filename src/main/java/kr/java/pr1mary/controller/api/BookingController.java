package kr.java.pr1mary.controller.api;

import jakarta.validation.Valid;
import kr.java.pr1mary.dto.api.request.BookingCreateRequest;
import kr.java.pr1mary.dto.api.response.ApiResponse;
import kr.java.pr1mary.dto.api.response.BookingCreateResponse;
import kr.java.pr1mary.dto.api.response.BookingHistoryResponse;
import kr.java.pr1mary.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings") // 공통 URL -> 수강 예약 관련 요청
public class BookingController {
    private final BookingService bookingService;

    // 👉 예약 생성 POST
    // URL: POST /api/bookings
    // Body: { "studentId": 1, "scheduleId": 10, "requestMessage": "..." }
    // 응답 : 201 Created + ApiResponse
    @PostMapping
    public ResponseEntity<ApiResponse<BookingCreateResponse>> createBooking(
            @RequestBody @Valid BookingCreateRequest request
    ){
        // 서비스 실행 - ID 반환
        Long bookingId = bookingService.registerBooking(request);

        BookingCreateResponse data = BookingCreateResponse.of(bookingId, "PENDING");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("예약 요청 성공 (대기 상태)", data));
    }

     // 👉 수업 예약 취소
     // URL: PATCH /api/bookings/{bookingId}/cancel?studentId=1
    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(
            @PathVariable Long bookingId,
            @RequestParam Long studentId
    ){
        bookingService.cancelBooking(studentId, bookingId);

        // 200 OK 메시지 전달
        return ResponseEntity.ok(ApiResponse.ok("예약 취소 성공"));
    }

     // 👉내 예약 조회
     // URL: GET /api/bookings/my?studentId=1
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<BookingHistoryResponse>>> getMyBookings(
            @RequestParam Long studentId
    ){
        List<BookingHistoryResponse> response = bookingService.getMyBookings(studentId);

        // 200 OK 데이터 전달
        return ResponseEntity.ok(ApiResponse.ok("예약 내역 조회 성공", response));
    }
}
