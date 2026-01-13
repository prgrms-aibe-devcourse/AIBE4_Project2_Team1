package kr.java.pr1mary;

import kr.java.pr1mary.entity.user.User;
import kr.java.pr1mary.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userAccountRepository.count() > 0) {
            return;
        }

        //관리자, 테스트용 계정 생성
        User admin = new User(
                "admin@admin",
                passwordEncoder.encode("admin1234"),
                "관리자",
                User.Role.ADMIN,
                User.Auth.LOCAL
        );
        userAccountRepository.save(admin);

        User userStudent = new User(
                "user_student@user",
                passwordEncoder.encode("user1234"),
                "학생사용자",
                User.Role.STUDENT,
                User.Auth.LOCAL
        );
        userAccountRepository.save(userStudent);

        User userTeacher = new User(
                "user_teacher@user",
                passwordEncoder.encode("user1234"),
                "강사사용자",
                User.Role.TEACHER,
                User.Auth.LOCAL
        );
        userAccountRepository.save(userTeacher);

        System.out.println("=== 초기 데이터 생성 완료 ===");
        System.out.println("관리자: admin@admin / admin1234");
        System.out.println("학생: user_student@user / user1234");
        System.out.println("강사: user_teacher@user / user1234");
        System.out.println("비밀번호 저장 형식: " + admin.getPassword().substring(0, 20) + "...");
    }
}
