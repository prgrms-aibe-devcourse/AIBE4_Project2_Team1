package kr.java.pr1mary.dto.api.response;

import kr.java.pr1mary.entity.lesson.Lesson;
import kr.java.pr1mary.entity.lesson.Subjects;

public record LessonDetailResponse(
        Long id,
        String title,
        String description,
        Subjects subjects,
        String mode,
        Long price,
        Double averageRating
) {
        public static LessonDetailResponse from(Lesson lesson) {
                return new LessonDetailResponse(
                        lesson.getId(),
                        lesson.getTitle(),
                        lesson.getDescription(),
                        lesson.getSubjects(),
                        lesson.getMode().toString(),
                        lesson.getPrice(),
                        lesson.getAverageRating()
                );
        }
}
