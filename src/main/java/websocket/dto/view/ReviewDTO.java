package websocket.dto.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Long userId;
    private Long lessonId;
    private Integer rating;
    private String comment;
}
