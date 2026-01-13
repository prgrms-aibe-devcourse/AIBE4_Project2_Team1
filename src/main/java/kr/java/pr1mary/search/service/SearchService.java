package kr.java.pr1mary.search.service;

import kr.java.pr1mary.entity.lesson.Lesson;
import kr.java.pr1mary.search.document.LessonDocument;
import kr.java.pr1mary.search.document.SearchFilter;
import kr.java.pr1mary.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    // 검색
    public Page<LessonDocument> searchWithFilter(String keyword, SearchFilter filter, Pageable pageable) {
        Criteria criteria = new Criteria();

        // 키워드 조건
        if (keyword != null && !keyword.isBlank()) {
            criteria = criteria.subCriteria(new Criteria("lessonName").contains(keyword)
                    .or("teacherName").contains(keyword));
        }

        // 필터 조건
        if (filter != null) {
            if (filter.getSubject() != null && !filter.getSubject().isBlank()) {
                // AND 조건 추가
                criteria = criteria.and(new Criteria("subject").contains(filter.getSubject()));
            }

            if (filter.getMode() != null && !filter.getMode().isBlank()) {
                criteria = criteria.and(new Criteria("mode").is(filter.getMode()));
            }
        }

        Query query = new CriteriaQuery(criteria).setPageable(pageable);

        SearchHits<LessonDocument> searchHits = elasticsearchOperations.search(query, LessonDocument.class);

        List<LessonDocument> list = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        return new PageImpl<>(list, pageable, searchHits.getTotalHits());
    }

    public void saveDocument(Lesson lesson){
        LessonDocument lessonDocument = LessonDocument.from(lesson);
        searchRepository.save(lessonDocument);
    }

    public void updateDocument(Lesson lesson){
        LessonDocument existingDocument = searchRepository.findByLessonId(lesson.getId());

        existingDocument.setLessonName(lesson.getTitle());
        existingDocument.setSubject(lesson.getSubjects().toString());
        existingDocument.setAverageRating(lesson.getAverageRating());
        existingDocument.setPrice(lesson.getPrice());
        existingDocument.setMode(lesson.getMode().toString());

        searchRepository.save(existingDocument);
    }

    public void deleteDocument(Long lessonId){
        LessonDocument lessonDocument = searchRepository.findByLessonId(lessonId);
        searchRepository.delete(lessonDocument);
    }

    public void reset(){
        searchRepository.deleteAll();
    }
}
