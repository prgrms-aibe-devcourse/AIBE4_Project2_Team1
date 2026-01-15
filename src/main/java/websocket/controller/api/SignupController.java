package websocket.controller.api;

import jakarta.mail.MessagingException;
import websocket.entity.user.User.Role;
import websocket.service.EmailSendService;
import websocket.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignupController {
    private final SignupService signupService;
    private final EmailSendService emailSendService;

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "signup", required = false) String signup,
            Model model) {

        if (error != null && message != null) {
            model.addAttribute("errorMessage", message);
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "로그아웃되었습니다.");
        }
        if ("success".equals(signup)) {
            model.addAttribute("signupMessage", "회원가입이 완료되었습니다. 로그인해주세요.");
        }

        return "pages/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "pages/signup";
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String username,
            @RequestParam Role role,
            Model model) {
        try {
            signupService.signup(email, password, username, role);
            return "redirect:/api/login?signup=success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/signup";
        }
    }

    @PostMapping("/signup/verify-email")
    @ResponseBody
    public ResponseEntity<String> sendVerificationEmail(@RequestBody Map<String, String> payload) {
        try {
            // 이메일 중복 체크
            if (signupService.checkEmailExists(payload.get("email"))) {
                return ResponseEntity.badRequest().body("이미 가입된 이메일입니다.");
            }
            emailSendService.sendAuthEmail(payload.get("email"));
            return ResponseEntity.ok("인증 메일이 발송되었습니다.");
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body("인증 메일 발송에 실패했습니다.");
        }
    }

    @PostMapping("/signup/verify-code")
    @ResponseBody
    public ResponseEntity<String> verifyEmailCode(@RequestBody Map<String, String> payload) {
        boolean isVerified = emailSendService.verifyEmailCode(payload.get("email"), payload.get("code"));
        if (isVerified) {
            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");
        }
    }


    @GetMapping("/logintest")
    public String loginTestPage() {
        return "pages/login/logintest";
    }
}
