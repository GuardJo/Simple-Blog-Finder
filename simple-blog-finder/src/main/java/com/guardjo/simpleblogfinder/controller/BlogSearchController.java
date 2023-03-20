package com.guardjo.simpleblogfinder.controller;

import com.guardjo.simpleblogfinder.constant.BlogSearchConstant;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchResponse;
import com.guardjo.simpleblogfinder.dto.SearchTermDto;
import com.guardjo.simpleblogfinder.service.BlogSearchService;
import com.guardjo.simpleblogfinder.service.SearchManagementService;
import com.guardjo.simpleblogfinder.util.RequestChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(BlogSearchConstant.REST_URL_PREFIX)
@Slf4j
public class BlogSearchController {
    private final BlogSearchService blogSearchService;
    private final RequestChecker requestChecker;
    private final SearchManagementService searchManagementService;

    public BlogSearchController(@Autowired BlogSearchService blogSearchService,
                                @Autowired RequestChecker requestChecker,
                                @Autowired SearchManagementService searchManagementService) {
        this.blogSearchService = blogSearchService;
        this.requestChecker = requestChecker;
        this.searchManagementService = searchManagementService;
    }

    @GetMapping(BlogSearchConstant.REQUEST_BLOG_SEARCH_URL)
    public KakaoBlogSearchResponse searchBlog(@RequestParam String searchValue,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = BlogSearchConstant.SEARCH_SORT_TYPE_ACCURACY) String sort,
                                              @RequestParam(defaultValue = "1") int page) throws Exception{
        log.info("[Test] Request Search Blogs, searchValue = {}, sortType = {}, pageSize = {}, pageNumber= {}",
                searchValue, sort, size, page);

        KakaoBlogSearchRequest request = KakaoBlogSearchRequest.of(searchValue, sort, page, size);
        requestChecker.blogSearchRequestValidate(request);

        KakaoBlogSearchResponse response = blogSearchService.searchBlogs(request);

        return response;
    }

    @GetMapping(BlogSearchConstant.REQUEST_BLOG_SEARCH_KEYWORD_TOP_TEN)
    public List<SearchTermDto> findSearchTermRanking() {
        log.info("[Test] Request SearchTerm TOP10 Ranking");

        return searchManagementService.findSearchTermRanking();
    }

}
