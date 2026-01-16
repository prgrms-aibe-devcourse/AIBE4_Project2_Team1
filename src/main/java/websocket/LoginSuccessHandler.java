package websocket;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import websocket.repository.UserRepository;

import java.io.IOException;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository; // DB 조회를 위해 필요

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 1. 현재 로그인한 유저의 이메일(username) 가져오기
        String email = authentication.getName();

        // 2. DB에서 해당 유저의 PK(id) 찾기
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."))
                .getId();

        // 3. 세션에 ID 저장 (가장 핵심!)
        HttpSession session = request.getSession();
        session.setAttribute("userId", userId);

        // 4. 로그인 후 이동할 페이지로 리다이렉트
        response.sendRedirect("/");
    }
}
