package com.guardjo.simpleblogfinder.service;

import com.guardjo.simpleblogfinder.constant.BlogSearchConstant;
import com.guardjo.simpleblogfinder.dto.kakao.KakaoBlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.kakao.KakaoBlogSearchResponse;
import com.guardjo.simpleblogfinder.util.kakaoBlogSearchRequestChecker;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
public class BlogSearchService {
    private final WebClient kakaoWebClient;
    private final kakaoBlogSearchRequestChecker kakaoBlogSearchRequestChecker;

    public BlogSearchService(WebClient kakaoWebClient, kakaoBlogSearchRequestChecker kakaoBlogSearchRequestChecker) {
        this.kakaoWebClient = kakaoWebClient;
        this.kakaoBlogSearchRequestChecker = kakaoBlogSearchRequestChecker;
    }

    public KakaoBlogSearchResponse searchBlogs(KakaoBlogSearchRequest request) {
        kakaoBlogSearchRequestChecker.blogSearchRequestValidate(request);

        log.info("[Test] Blog Searching... query = {}", request.getQuery());
        URI uri = UriComponentsBuilder
                .fromUriString(BlogSearchConstant.KAKAO_BLOG_SEARCH_API_URL)
                .queryParam("query", request.getQuery())
                .queryParam("sort", request.getSort())
                .queryParam("page", request.getPage())
                .queryParam("size", request.getSize())
                .encode(CharsetUtil.UTF_8)
                .build()
                .toUri();

        KakaoBlogSearchResponse response = kakaoWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(KakaoBlogSearchResponse.class)
                .block();

        log.info("[Test] Searched Blog Data, query = {}", request.getQuery());
        return response;
    }
}
