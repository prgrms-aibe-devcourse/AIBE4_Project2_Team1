package websocket.search.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchFilter {
    private String subject;      // 과목 (예: "수학", "영어")
    private String mode;         // 수업 방식 (예: "ONLINE", "OFFLINE")
}
