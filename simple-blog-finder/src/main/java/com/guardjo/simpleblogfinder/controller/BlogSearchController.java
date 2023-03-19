package com.guardjo.simpleblogfinder.controller;

import com.guardjo.simpleblogfinder.constant.BlogSearchConstant;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchResponse;
import com.guardjo.simpleblogfinder.service.BlogSearchService;
import com.guardjo.simpleblogfinder.util.RequestChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BlogSearchConstant.REST_URL_PREFIX)
@Slf4j
public class BlogSearchController {
    private final BlogSearchService blogSearchService;
    private final RequestChecker requestChecker;

    public BlogSearchController(@Autowired BlogSearchService blogSearchService,
                                @Autowired RequestChecker requestChecker) {
        this.blogSearchService = blogSearchService;
        this.requestChecker = requestChecker;
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
}
