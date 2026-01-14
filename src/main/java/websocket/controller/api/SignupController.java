package websocket.controller.api;

import websocket.entity.user.User.Role;
import websocket.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignupController {
    private final SignupService signupService;

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null && message != null) {
            model.addAttribute("errorMessage", message);
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "로그아웃되었습니다.");
        }

        return "pages/login/login-form";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "pages/login/signup-form";
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
            return "pages/login/signup-form";
        }
    }

    @GetMapping("/logintest")
    public String loginTestPage() {
        return "pages/login/logintest";
    }
}
