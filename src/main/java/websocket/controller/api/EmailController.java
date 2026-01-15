package websocket.controller.api;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import websocket.dto.view.EmailRequestDto;
import websocket.service.EmailSendService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailSendService emailSendService;

    /* Send Email: 인증번호 전송 버튼 click */
    @PostMapping("/signup/email")
    public ResponseEntity<Map<String, String>> mailSend(@RequestBody @Valid EmailRequestDto emailRequestDto) {
        Map<String, String> response = new HashMap<>();
        try {
            String code = emailSendService.sendAuthEmail(emailRequestDto.getEmail());
            response.put("message", "인증 메일이 발송되었습니다.");
            response.put("code", code); // 클라이언트 테스트용으로 코드를 포함 (실제 운영에서는 제외하는 것이 보안상 좋음)
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            response.put("message", "인증 메일 발송에 실패했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}