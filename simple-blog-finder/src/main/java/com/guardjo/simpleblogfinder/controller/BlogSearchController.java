package com.guardjo.simpleblogfinder.controller;

import com.guardjo.simpleblogfinder.constant.BlogSearchConstant;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchResponse;
import com.guardjo.simpleblogfinder.service.BlogSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BlogSearchConstant.REST_URL_PREFIX)
@Slf4j
public class BlogSearchController {
    private final BlogSearchService blogSearchService;

    public BlogSearchController(@Autowired BlogSearchService blogSearchService) {
        this.blogSearchService = blogSearchService;
    }

    @GetMapping(BlogSearchConstant.REQUEST_BLOG_SEARCH_URL)
    public KakaoBlogSearchResponse searchBlog(@RequestParam String searchValue,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = BlogSearchConstant.SEARCH_SORT_TYPE_ACCURACY) String sort,
                                              @RequestParam(defaultValue = "1") int page) {
        log.info("[Test] Request Search Blogs, searchValue = {}, sortType = {}, pageSize = {}, pageNumber= {}",
                searchValue, sort, size, page);

        KakaoBlogSearchRequest request = KakaoBlogSearchRequest.of(searchValue, sort, page, size);

        KakaoBlogSearchResponse response = blogSearchService.searchBlogs(request);

        return response;
    }
}
