package kr.java.pr1mary.controller.view;

import kr.java.pr1mary.search.document.LessonDocument;
import kr.java.pr1mary.search.document.SearchFilter;
import kr.java.pr1mary.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchViewController {
    private final SearchService searchService;

    // 검색 창 페이지
    @GetMapping
    public String search(@RequestParam(required = false) String keyword,
                         @ModelAttribute SearchFilter searchFilter,
                         @PageableDefault(size = 10) Pageable pageable, Model model) {
        if ((keyword != null && !keyword.isBlank()) || hasFilterCondition(searchFilter)) {
            Page<LessonDocument> results = searchService.searchWithFilter(keyword, searchFilter, pageable);

            // 3. 로그로 결과 개수 확인
            log.info("검색 키워드: {}, 필터: {}", keyword, searchFilter);

            model.addAttribute("results", results);
        }

        model.addAttribute("filter", searchFilter);
        model.addAttribute("keyword", keyword);

        return "pages/search";
    }

    // 필터 조건이 있는지 확인함
    private boolean hasFilterCondition(SearchFilter filter) {
        return filter.getSubject() != null || filter.getMode() != null;
    }
}
