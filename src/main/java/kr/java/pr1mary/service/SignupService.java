package kr.java.pr1mary.service;

import kr.java.pr1mary.entity.user.User;
import kr.java.pr1mary.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(String email, String rawPassword, String name, User.Role role) {
        if (userAccountRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User(
                email,
                encodedPassword,
                name,
                role,
                User.Auth.LOCAL
        );

        userAccountRepository.save(user);
    }
}
