package kr.java.pr1mary.service;

import jakarta.persistence.EntityNotFoundException;
import kr.java.pr1mary.dto.api.request.LessonRequest;
import kr.java.pr1mary.dto.api.response.LessonDetailResponse;
import kr.java.pr1mary.dto.api.response.LessonResponse;
import kr.java.pr1mary.entity.lesson.Lesson;
import kr.java.pr1mary.entity.user.User;
import kr.java.pr1mary.repository.LessonRepository;
import kr.java.pr1mary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LessonService {
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    @Transactional
    public LessonResponse save(LessonRequest lessonRequest, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        
        // TODO: 유저가 선생님인지 확인

        Lesson lesson = Lesson.create(lessonRequest, user);
        Lesson saved = lessonRepository.save(lesson);

        return LessonResponse.from(saved);
    }
    
    // TODO: 인기 과외 목록 조회

    // 과외 상세
    public LessonDetailResponse getLessonDetail(Long lessonId){
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("과외를 찾을 수 없습니다."));

        return LessonDetailResponse.from(lesson);
    }

    // 선생님 별 과외 목록 조회
    public List<LessonResponse> getTeacherLessons(Long teacherId){
        return lessonRepository.findByUser_Id(teacherId).stream()
                .map(LessonResponse::from).collect(Collectors.toList());
    }

    // 과외 삭제
    @Transactional
    public void deleteLesson(Long lessonId, Long userId){
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        if(!Objects.equals(lesson.getUser().getId(), user.getId())){
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        lessonRepository.delete(lesson);
    }
}
