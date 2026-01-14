package websocket.service;

import websocket.dto.view.TeacherDTO;
import websocket.entity.lesson.Booking;
import websocket.entity.lesson.Lesson;
import websocket.entity.lesson.Subject;
import websocket.entity.lesson.Subjects;
import websocket.entity.user.Review;
import websocket.entity.user.TeacherProfile;
import websocket.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websocket.repository.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherProfileService {
    private final UserRepository userRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final SubjectRepository subjectRepository;
    private final websocket.repository.ReviewRepository reviewRepository;
    private final websocket.repository.LessonRepository lessonRepository;
    private final websocket.repository.BookingRepository bookingRepository;

    @Value("${file.path-teacher}")
    private String teacherPath;

    // 교사 프로필 수정
    public void updateTeacherProfile(TeacherDTO dto) {
        TeacherProfile profile = getProfileByTeacherId(dto.getId());

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + dto.getImage().getOriginalFilename();
        System.out.println("이미지 이름: " + imageFileName);

        Path directoryPath = Paths.get(teacherPath);
        Path imageFilePath = directoryPath.resolve(imageFileName);

        try {
            if (Files.notExists(directoryPath)) Files.createDirectories(directoryPath);
            Files.write(imageFilePath, dto.getImage().getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        profile.setImageUrl(imageFileName);
        profile.setRegionCode(dto.getRegionCode());
        profile.setBio(dto.getIntroduce());
    }

//    // 프로필 사진 등록 -> 이미지 경로 출력
//    public String setTeacherImage(TeacherDTO dto) {
//        TeacherProfile profile = getProfileByTeacherId(dto.getId());
//
//        UUID uuid = UUID.randomUUID();
//        String imageFileName = uuid + "_" + dto.getId();
//        System.out.println("이미지 이름: " + imageFileName);
//
//        Path imageFilePath = Paths.get(teacherPath, imageFileName);
//
//        try {
//            Files.write(imageFilePath, dto.getImage().getBytes());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        profile.setImageUrl(imageFileName);
//        return imageFileName;
//    }
//
//    // 교사 한줄 소개 등록
//    public void setTeacherIntroduce(TeacherDTO dto) {
//        TeacherProfile profile = getProfileByTeacherId(dto.getId());
//        profile.setBio(dto.getIntroduce());
//    }

    // 담당 과목 설정
    public void saveSubject(TeacherDTO dto) {
        User user = getProfileByTeacherId(dto.getId()).getUser();
        for (Subjects s: dto.getSubjects()) {
            if (subjectRepository.existsByUserAndSubjects(user, s)) return;
            Subject subject = new Subject();
            subject.setUser(user);
            subject.setSubjects(s);
            subjectRepository.save(subject);
        }
    }

    // 교사 id로 교사 정보 불러오기 => 없다면 빈(empty) 프로필 생성
    public TeacherProfile getProfileByTeacherId(Long id) {
        return teacherProfileRepository.findByUser_Id(id)
                .orElseGet(() -> teacherProfileRepository.save(new TeacherProfile(
                        userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."))
                )));
    }

    // 리뷰 평점 가져오기
    public Double getAverageRating(Long teacherId) {
        List<Review> reviews = getReviews(teacherId);
        double sum = 0.0, count = 0.0;
        for (Review review : reviews) {
            sum += review.getRating();
            count++;
        }
        return count == 0.0 ? 0.0 : sum / count;
    }

    // 리뷰 가져오기
    public List<Review> getReviews(Long teacherId) {
        return reviewRepository.findByTeacherId(teacherId);
    }

    // 모든 수업 조회
    public List<Lesson> getAllLessons(Long teacherId) {
        return lessonRepository.findByUser_Id(teacherId);
    }

    // 모든 예약 조회
    public List<Booking> getAllBookings(Long teacherId) {
        return bookingRepository.findAllByTeacherId(teacherId);
    }
}
