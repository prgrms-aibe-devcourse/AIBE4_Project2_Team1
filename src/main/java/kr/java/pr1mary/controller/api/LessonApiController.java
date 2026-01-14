package kr.java.pr1mary.controller.api;

import jakarta.validation.Valid;
import kr.java.pr1mary.dto.api.response.LessonDetailResponse;
import kr.java.pr1mary.dto.api.response.LessonResponse;
import kr.java.pr1mary.dto.api.request.LessonRequest;
import kr.java.pr1mary.dto.api.request.LessonUpdateRequest;
import kr.java.pr1mary.entity.lesson.Subjects;
import kr.java.pr1mary.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonApiController {
    private final LessonService lessonService;

    // 과외 생성
    @PostMapping
    public ResponseEntity<LessonResponse> create(@Valid @RequestBody LessonRequest lessonRequest,
                                                 @RequestParam Long teacherId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.saveLesson(lessonRequest, teacherId));
    }

    // 과외 수정
    @PutMapping
    public ResponseEntity<Void> updateLesson(@Valid @RequestBody LessonUpdateRequest lessonUpdateRequest,
                                             @RequestParam Long teacherId){
        lessonService.updateLesson(lessonUpdateRequest, teacherId);

        return ResponseEntity.noContent().build();
    }

    // 과외 삭제
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId,
                                             @RequestParam Long teacherId){
        lessonService.deleteLesson(lessonId, teacherId);

        return ResponseEntity.noContent().build();
    }

    // 과외 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<LessonDetailResponse> getLessonDetailApi(@PathVariable Long id){
        // 테스트용 가짜 데이터
        LessonDetailResponse response = new LessonDetailResponse(
                1L,
                "고등 수학 완전 정복 (수1, 수2)",   // title
                "이 수업은 수학 기초가 부족한 학생들을 위해...", // description
                Subjects.MATH,                     // subjects (Enum 가정)
                "ONLINE",                          // mode
                65000L,                            // price (Long 타입)
                4.8,                                // averageRating
                90
        );
        return ResponseEntity.ok(response);
    }
}
