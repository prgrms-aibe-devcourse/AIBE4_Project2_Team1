package websocket.search.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import websocket.entity.lesson.Lesson;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Setter
@Document(indexName = "lessons")
@Setting(settingPath = "elasticsearch/lesson-setting.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LessonDocument {
    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long lessonId;

    /** 과외 이름 */
    @Field(type = FieldType.Text)
    private String lessonName;

    /** 선생님 이름 */
    @Field(type = FieldType.Text)
    private String teacherName;

    /** 과목 */
    @Field(type = FieldType.Text)
    private String subject;

    /** 과목이름 */
    @Field(type = FieldType.Text)
    private String subjectName;

    /** 수업 방식 */
    @Field(type = FieldType.Text)
    private String mode;

    /** 별점 */
    @Field(type = FieldType.Double)
    private Double averageRating;

    /** 가격 */
    @Field(type = FieldType.Long)
    private Long price;

    public static LessonDocument from(Lesson lesson){
        LessonDocument lessonDocument = new LessonDocument();

        lessonDocument.setLessonId(lesson.getId());
        lessonDocument.setLessonName(lesson.getTitle());
        lessonDocument.setTeacherName(lesson.getUser().getName());
        lessonDocument.setSubject(lesson.getSubjects().toString());
        lessonDocument.setSubjectName(lesson.getSubjects().getDesc());
        lessonDocument.setAverageRating(lesson.getAverageRating());
        lessonDocument.setPrice(lesson.getPrice());
        lessonDocument.setMode(lesson.getMode().toString());

        return lessonDocument;
    }
}
