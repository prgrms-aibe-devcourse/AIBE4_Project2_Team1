package kr.java.pr1mary.controller.view;

import kr.java.pr1mary.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchViewController {
    private final SearchService searchService;

    // 검색 창 페이지
    @GetMapping
    public String search(@RequestParam(required = false) String keyword, Model model) {

        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("results", searchService.searchByKeyword(keyword));
        }

        model.addAttribute("keyword", keyword);
        return "pages/search";
    }
}
