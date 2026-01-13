package kr.java.pr1mary;

import kr.java.pr1mary.entity.user.User;
import kr.java.pr1mary.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import kr.java.pr1mary.dto.api.request.LessonRequest;
import kr.java.pr1mary.entity.lesson.Lesson;
import kr.java.pr1mary.entity.lesson.Subjects;
import kr.java.pr1mary.entity.user.TeacherProfile;
import kr.java.pr1mary.repository.TeacherProfileRepository;
import kr.java.pr1mary.repository.UserRepository;
import kr.java.pr1mary.search.service.SearchService;
import kr.java.pr1mary.service.LessonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final LessonService lessonService;
    private final TeacherProfileRepository teacherProfileRepository;
    private final SearchService searchService;

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
        if (userRepository.count() > 0) {
            log.info("데이터가 이미 존재합니다.");
            return;
        }

        log.info("테스트 데이터 초기화...");

        searchService.reset();

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

        User t = userRepository.save(teacher);
        userRepository.save(student);

        TeacherProfile teacherProfile = new TeacherProfile();
        teacherProfile.setUser(t);
        teacherProfile.setBio("hello");
        teacherProfile.setRegionCode("312");
        teacherProfile.setSchoolLevel(TeacherProfile.Level.HIGH);

        teacherProfileRepository.save(teacherProfile);

        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setTitle("test1");
        lessonRequest.setDescription("test1");
        lessonRequest.setSubjects(Subjects.MATH);
        lessonRequest.setMode(Lesson.Mode.ONLINE);
        lessonRequest.setPrice(1000L);

        lessonService.saveLesson(lessonRequest, t.getId());

        LessonRequest lessonRequest2 = new LessonRequest();
        lessonRequest2.setTitle("test2");
        lessonRequest2.setDescription("test2");
        lessonRequest2.setSubjects(Subjects.ENGLISH);
        lessonRequest2.setMode(Lesson.Mode.OFFLINE);
        lessonRequest2.setPrice(1000L);

        lessonService.saveLesson(lessonRequest2, t.getId());

        log.info("초기화 완료!");
    }
}
