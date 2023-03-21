package com.guardjo.simpleblogfinder.controller;

import com.guardjo.simpleblogfinder.constant.BlogSearchConstant;
import com.guardjo.simpleblogfinder.dto.BlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.BlogSearchResponse;
import com.guardjo.simpleblogfinder.dto.kakao.KakaoBlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.SearchTermDto;
import com.guardjo.simpleblogfinder.service.BlogSearchService;
import com.guardjo.simpleblogfinder.service.SearchManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(BlogSearchConstant.REST_URL_PREFIX)
@Slf4j
public class BlogSearchController {
    private final BlogSearchService blogSearchService;
    private final SearchManagementService searchManagementService;

    public BlogSearchController(@Autowired BlogSearchService blogSearchService,
                                @Autowired SearchManagementService searchManagementService) {
        this.blogSearchService = blogSearchService;
        this.searchManagementService = searchManagementService;
    }

    @GetMapping(BlogSearchConstant.REQUEST_BLOG_SEARCH_URL)
    public BlogSearchResponse searchBlog(@RequestParam(defaultValue = BlogSearchConstant.KAKAO_API) String searchApiType,
                                         @RequestParam String searchValue,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = BlogSearchConstant.SEARCH_SORT_TYPE_ACCURACY) String sort,
                                         @RequestParam(defaultValue = "1") int page) throws Exception {
        log.info("[Test] Request Search Blogs, searchValue = {}, sortType = {}, pageSize = {}, pageNumber= {}",
                searchValue, sort, size, page);

        BlogSearchRequest request = null;
        BlogSearchResponse response = null;

        // TODO 향후 다른 블로그 검색 API 호출 기능 추가 시 관련 작업 추가 (default = KAKAO)
        switch (searchApiType) {
            default:
                request = KakaoBlogSearchRequest.of(searchValue, sort, page, size);
                response = blogSearchService.searchBlogs((KakaoBlogSearchRequest) request);
        }

        SearchTermDto searchTermDto = searchManagementService.findSearchTerm(searchValue);
        log.debug("[Test] Searched! searchTermDto = {}", searchTermDto.toString());

        return response;
    }

    @GetMapping(BlogSearchConstant.REQUEST_BLOG_SEARCH_KEYWORD_TOP_TEN)
    public List<SearchTermDto> findSearchTermRanking() {
        log.info("[Test] Request SearchTerm TOP10 Ranking");

        return searchManagementService.findSearchTermRanking();
    }
}
