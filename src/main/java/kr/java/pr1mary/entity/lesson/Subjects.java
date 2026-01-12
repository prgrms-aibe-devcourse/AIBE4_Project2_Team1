package kr.java.pr1mary.entity.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Subjects {
    MATH("수학"),
    ENGLISH("영어"),
    KOREAN("국어"),
    HISTORY("역사"),
    SCIENCE("과학");

    private final String desc;
}
