package websocket.dto.view;

import websocket.entity.lesson.Subjects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class TeacherDTO {
    private Long id;
    private MultipartFile image;
    private String introduce;
    private Subjects subjects;
}
