package websocket.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailSendService {

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // 랜덤 인증번호 생성
    private String makeRandomNum() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }
        return randomNumber;
    }

    // 이메일 전송 및 인증번호 저장
    public String sendAuthEmail(String toEmail) throws MessagingException {
        String authNumber = makeRandomNum();
        String title = "StudyLink 회원 가입을 위한 인증 이메일";
        String content =
                "이메일을 인증하기 위한 절차입니다." +
                "<br><br>" +
                "인증 번호는 " + authNumber + "입니다." +
                "<br>" +
                "회원 가입 폼에 해당 번호를 입력해주세요.";

        mailSend(fromEmail, toEmail, title, content);

        // Redis에 3분 동안 이메일과 인증 코드 저장
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(toEmail, authNumber, 3, TimeUnit.MINUTES); // 유효 시간 3분

        return authNumber;
    }

    // 메일 전송 로직
    private void mailSend(String setFrom, String toMail, String title, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(setFrom);
        helper.setTo(toMail);
        helper.setSubject(title);
        helper.setText(content, true);
        javaMailSender.send(message);
    }

    // 인증번호 검증
    public boolean verifyEmailCode(String email, String code) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String storedCode = valueOperations.get(email);

        if (storedCode != null && storedCode.equals(code)) {
            redisTemplate.delete(email); // 인증 성공 시 Redis에서 코드 삭제
            return true;
        }
        return false;
    }
}