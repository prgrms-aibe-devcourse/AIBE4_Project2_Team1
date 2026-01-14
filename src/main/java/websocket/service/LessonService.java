package websocket.service;

import jakarta.persistence.EntityNotFoundException;
import websocket.dto.api.response.LessonDetailResponse;
import websocket.dto.api.response.LessonResponse;
import websocket.dto.api.request.LessonRequest;
import websocket.dto.api.request.LessonUpdateRequest;
import websocket.entity.lesson.Lesson;
import websocket.entity.user.User;
import websocket.repository.LessonRepository;
import websocket.repository.TeacherProfileRepository;
import websocket.repository.UserRepository;
import websocket.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final TeacherProfileRepository teacherProfileRepository;
    private final SearchService searchService;

    public Lesson getLesson(Long lessonId){
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("과외를 찾을 수 없습니다."));
    }

    // 과외 저장
    @Transactional
    public LessonResponse saveLesson(LessonRequest request, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        if(!teacherProfileRepository.existsTeacherProfileByUser_Id(userId)){
            throw new IllegalArgumentException("선생님이 아닙니다.");
        }

        Lesson lesson = Lesson.create(request, user);
        Lesson saved = lessonRepository.save(lesson);

        // elasticsearch document 저장
        searchService.saveDocument(saved);

        return LessonResponse.from(saved);
    }

    // 과외 정보 수정
    @Transactional
    public LessonResponse updateLesson(LessonUpdateRequest request, Long userId){
        Lesson lesson = lessonRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("과외를 찾을 수 없습니다."));

        if(!Objects.equals(lesson.getUser().getId(), userId)){
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        lesson.update(request);

        // elasticsearch document 저장
        searchService.updateDocument(lesson);

        return LessonResponse.from(lesson);
    }

    public Page<LessonResponse> getSearchLessons(String content, Pageable pageable){
        return lessonRepository.findLessonsByContaining(content, pageable).map(LessonResponse::from);
    }
    
    // 인기 과외 목록 조회
    public List<LessonResponse> getPopularLessons(){
        return lessonRepository.findPopularLessons().stream()
                .map(LessonResponse::from).collect(Collectors.toList());
    }

    // 과외 상세 조회
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

        if(!Objects.equals(lesson.getUser().getId(), userId)){
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        // elasticsearch document 삭제
        searchService.deleteDocument(lesson.getId());

        lessonRepository.delete(lesson);
    }
}
