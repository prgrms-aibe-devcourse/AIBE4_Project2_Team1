package kr.java.pr1mary.search.repository;

import kr.java.pr1mary.search.document.LessonDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchRepository extends ElasticsearchRepository<LessonDocument, Long> {
    LessonDocument findByLessonId(Long lessonId);

    void deleteAll();
}
