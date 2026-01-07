package kr.java.pr1mary;

import kr.java.pr1mary.entity.lesson.Lesson;
import kr.java.pr1mary.entity.lesson.Subjects;
import kr.java.pr1mary.entity.user.User;
import kr.java.pr1mary.repository.LessonRepository;
import kr.java.pr1mary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("데이터가 이미 존재합니다.");
            return;
        }

        log.info("테스트 데이터 초기화...");

        User teacher = new User();
        teacher.setEmail("teacher@naver.com");
        teacher.setPassword("teacher123");
        teacher.setName("teacher");
        teacher.setAuth(User.Auth.LOCAL);
        teacher.setRole(User.Role.TEACHER);

        User student = new User();
        student.setEmail("student@naver.com");
        student.setPassword("student123");
        student.setName("student");
        student.setAuth(User.Auth.LOCAL);
        student.setRole(User.Role.STUDENT);

        userRepository.saveAll(List.of(teacher, student));

        Lesson lesson = new Lesson();
        lesson.setUser(teacher);
        lesson.setTitle("lesson1");
        lesson.setDescription("lesson1");
        lesson.setPrice(10000L);
        lesson.setAverageRating(5.0);
        lesson.setMode(Lesson.Mode.OFFLINE);
        lesson.setSubjects(Subjects.ENGLISH);

        lessonRepository.save(lesson);

        log.info("초기화 완료!");
    }
}
