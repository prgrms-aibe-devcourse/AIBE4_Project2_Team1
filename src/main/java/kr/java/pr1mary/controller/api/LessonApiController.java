package kr.java.pr1mary.controller.api;

import jakarta.validation.Valid;
import kr.java.pr1mary.dto.api.response.LessonResponse;
import kr.java.pr1mary.dto.api.request.LessonRequest;
import kr.java.pr1mary.dto.api.request.LessonUpdateRequest;
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
    public ResponseEntity<LessonResponse> create(@Valid @RequestBody LessonRequest lessonRequest) {
        // TODO: userId 가져오기
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.saveLesson(lessonRequest, 1L));
    }

    // 과외 수정
    @PutMapping
    public ResponseEntity<Void> updateLesson(@Valid @RequestBody LessonUpdateRequest lessonUpdateRequest){
        // TODO: userId 가져오기
        lessonService.updateLesson(lessonUpdateRequest, 1L);

        return ResponseEntity.noContent().build();
    }

    // 과외 삭제
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId){
        // TODO: userId 가져오기
        lessonService.deleteLesson(lessonId, 1L);

        return ResponseEntity.noContent().build();
    }
}
