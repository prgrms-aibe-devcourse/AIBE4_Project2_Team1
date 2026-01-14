package websocket.dto.api.request;

import jakarta.validation.constraints.NotBlank;

public record BookingCancelRequest(
        @NotBlank(message = "취소 사유는 필수입니다,")
        String cancelReason
) {
}
