package com.guardjo.simpleblogfinder.controller;

import com.guardjo.simpleblogfinder.constant.BlogSearchConstant;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchResponse;
import lombok.extern.slf4j.Slf4j;
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
    @GetMapping(BlogSearchConstant.REQUEST_BLOG_SEARCH_URL)
    public KakaoBlogSearchResponse searchBlog(@RequestParam String searchValue,
                                              @PageableDefault(size = 10, sort = BlogSearchConstant.SEARCH_SORT_TYPE_ACCURACY, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("[Test] Request Search Blogs, query = {}", searchValue);
        return null;
    }
}
