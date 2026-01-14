package websocket.dto.view;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class StudentDTO {
    private Long id;
    private MultipartFile image;
    private String introduce;
}
