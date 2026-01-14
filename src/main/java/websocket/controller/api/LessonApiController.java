package websocket.controller.api;

import jakarta.validation.Valid;
import websocket.dto.api.response.LessonDetailResponse;
import websocket.dto.api.response.LessonResponse;
import websocket.dto.api.request.LessonRequest;
import websocket.dto.api.request.LessonUpdateRequest;
import websocket.entity.lesson.Subjects;
import websocket.service.LessonService;
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
        LessonDetailResponse response = lessonService.getLessonDetail(id);
        return ResponseEntity.ok(response);
    }
}
