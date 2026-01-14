package websocket.dto.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import websocket.entity.lesson.Lesson;
import websocket.entity.lesson.Subjects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonUpdateRequest {
    @NotNull
    private Long id;

    @NotBlank(message = "수업 제목은 필수입니다.")
    @Size(max = 50, message = "수업 제목은 50자 이내여야 합니다.")
    private String title;

    @NotBlank(message = "수업 설명을 입력해주세요.")
    private String description;

    @NotNull(message = "과목을 선택해주세요.")
    private Subjects subjects;

    @NotNull(message = "수업 방식을 선택해주세요.")
    private Lesson.Mode mode;

    @NotNull(message = "가격을 입력해주세요.")
    @Min(value = 1000)
    private Long price;
}
