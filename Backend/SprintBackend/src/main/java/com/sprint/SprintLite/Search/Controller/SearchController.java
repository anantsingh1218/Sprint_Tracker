package com.sprint.SprintLite.Search.Controller;

import com.sprint.SprintLite.Search.Dto.SearchResultDto;
import com.sprint.SprintLite.Search.Service.SearchService;
import com.sprint.SprintLite.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public List<SearchResultDto> search(
            @RequestParam("q") String q
    ) {
        return searchService.globalSearch(q);
    }

}
