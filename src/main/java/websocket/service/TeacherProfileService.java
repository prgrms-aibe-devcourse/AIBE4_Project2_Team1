package websocket.service;

import websocket.dto.view.TeacherDTO;
import websocket.entity.lesson.Booking;
import websocket.entity.lesson.Lesson;
import websocket.entity.lesson.Subject;
import websocket.entity.user.Review;
import websocket.entity.user.TeacherProfile;
import websocket.entity.user.User;
import kr.java.sse_websocket.repository.*;
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
    private final TeacherProfileRepository teacherProfileRepository;
    private final SubjectRepository subjectRepository;
    private final ReviewRepository reviewRepository;
    private final LessonRepository lessonRepository;
    private final BookingRepository bookingRepository;


    @Value("${file.path-teacher}")
    private String teacherPath;

    // 프로필 사진 등록 -> 이미지 경로 출력
    public String setTeacherImage(TeacherDTO dto) {
        TeacherProfile profile = getProfileByTeacherId(dto.getId());

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + dto.getId();
        System.out.println("이미지 이름: " + imageFileName);

        Path imageFilePath = Paths.get(teacherPath, imageFileName);

        try {
            Files.write(imageFilePath, dto.getImage().getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        profile.setImageUrl(imageFileName);
        return imageFileName;
    }

    // 교사 한줄 소개 등록
    public void setTeacherIntroduce(TeacherDTO dto) {
        TeacherProfile profile = getProfileByTeacherId(dto.getId());
        profile.setBio(dto.getIntroduce());
    }

    // 담당 과목 설정
    public void saveSubject(TeacherDTO dto) {
        User user = getProfileByTeacherId(dto.getId()).getUser();
        if (subjectRepository.existsByUserAndSubjects(user, dto.getSubjects())) return;
        Subject subject = new Subject();
        subject.setUser(user);
        subject.setSubjects(dto.getSubjects());
        subjectRepository.save(subject);
    }

    // 교사 id로 교사 정보 불러오기
    public TeacherProfile getProfileByTeacherId(Long id) {
        return teacherProfileRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없음"));
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
