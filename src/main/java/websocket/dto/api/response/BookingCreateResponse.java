package websocket.dto.api.response;

// 👉 학생의 수강 신청 결과 통보
public record BookingCreateResponse(
        Long bookingId,
        String status
) {
    public static BookingCreateResponse of (Long bookingId, String status){
        return new BookingCreateResponse(bookingId, status);
    }
}
