package kr.java.pr1mary.dto.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;// HTTP 상태 코드
    private String message; // 응답 메시지 - 예약 성공 / 조회 성공 등
    private T data; // 실제 데이터(없으면 null)

    // 200 OK 응답을 위한 메서드
    public static <T> ApiResponse<T> ok(String message, T data){
        return new ApiResponse<>(200, message, data);
    }

    // 200 OK(데이터가 없을 때 - 취소 성공 등)
    public static <T> ApiResponse<T> ok(String message){
        return new ApiResponse<>(200, message, null);
    }

    // 201 Created 응답을 위한 메서드
    public static <T> ApiResponse<T> created(String message, T data){
        return new ApiResponse<>(201, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(400, message, null);
    }
}
