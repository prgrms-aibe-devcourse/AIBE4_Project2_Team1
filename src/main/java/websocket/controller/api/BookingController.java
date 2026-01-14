package websocket.controller.api;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import websocket.dto.api.request.BookingCancelRequest;
import websocket.dto.api.request.BookingCreateRequest;
import websocket.dto.api.response.ApiResponse;
import websocket.dto.api.response.BookingCreateResponse;
import websocket.dto.api.response.BookingHistoryResponse;
import websocket.entity.CustomUserDetails;
import websocket.service.BookingService;
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

    // ==========================================
    //  [학생 기능] 예약 관리 (예약 요청 / 예약 취소 / 예약 조회)
    // ==========================================

    // 👉 수업 예약 요청 POST
    // URL: POST /api/bookings
    // Body: { "studentId": 1, "scheduleId": 10, "requestMessage": "..." }
    // 응답 : 201 Created + ApiResponse
    @PostMapping
    public ResponseEntity<ApiResponse<BookingCreateResponse>> createBooking(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid BookingCreateRequest request
    ){
        Long bookingId = bookingService.registerBooking(userDetails.getId(), request);
        BookingCreateResponse data = BookingCreateResponse.of(bookingId, "PENDING");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("수강 신청 성공 (대기 상태)", data));
    }

     // 👉 수업 예약 취소
     // URL: PATCH /api/bookings/9001/cancel-student?studentId=2001
    @PatchMapping("/{bookingId}/cancel-student")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long bookingId
    ){
        bookingService.cancelBooking(userDetails.getId(), bookingId);
        return ResponseEntity.ok(ApiResponse.ok("수강 신청 취소 성공"));
    }

     // 👉내 예약 조회
     // URL: GET /api/bookings/my?studentId=2001
    @GetMapping("/courseHistory")
    public ResponseEntity<ApiResponse<List<BookingHistoryResponse>>> getMyBookings(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        List<BookingHistoryResponse> response = bookingService.getMyBookings(userDetails.getId());

        // 200 OK 데이터 전달
        return ResponseEntity.ok(ApiResponse.ok("수강 신청 내역 조회 성공", response));
    }

    // ==========================================
    //  [선생님 기능] 예약 관리 (수락 / 거절 / 취소)
    // ==========================================
    // 👉 예약 수락 PENDING -> CONFIRMED
    @PatchMapping("/{bookingId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptBooking(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long bookingId
    ){
        bookingService.acceptBooking(userDetails.getId(), bookingId);
        return ResponseEntity.ok(ApiResponse.ok("수강신청이 완료되었습니다."));
    }

    // 👉 예약 거절 PENDING -> REJECTED
    @PatchMapping("/{bookingId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectBooking(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long bookingId
    ){
        bookingService.rejectBooking(userDetails.getId(), bookingId);
        return ResponseEntity.ok(ApiResponse.ok("수강 신청이 거절되었습니다."));
    }

    // 👉 확정된 예약 취소 CONFIRMED -> CANCELLED_BY_TEACHER
    @PatchMapping("/{bookingId}/cancel-teacher")
    public ResponseEntity<ApiResponse<Void>> cancelBookingByTeacher(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long bookingId,
            @RequestBody @Valid BookingCancelRequest request
    ){
        bookingService.cancelBookingByTeacher(userDetails.getId(), bookingId, request.cancelReason());
        return ResponseEntity.ok(ApiResponse.ok("수업 예약이 취소되었습니다."));
    }
}
