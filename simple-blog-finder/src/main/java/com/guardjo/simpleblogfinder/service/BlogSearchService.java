package com.guardjo.simpleblogfinder.service;

import com.guardjo.simpleblogfinder.constant.BlogSearchConstant;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class BlogSearchService {
    private final WebClient webClient;

    public BlogSearchService(@Autowired WebClient webClient) {
        this.webClient = webClient;
    }

    public KakaoBlogSearchResponse searchBlogs(KakaoBlogSearchRequest request) {
        URI uri = UriComponentsBuilder
                .fromUriString(BlogSearchConstant.KAKAO_BLOG_SEARCH_API_URL)
                .queryParam("query", request.getQuery())
                .queryParam("sort", request.getSort())
                .queryParam("page", request.getPage())
                .queryParam("size", request.getSize())
                .build()
                .toUri();

        KakaoBlogSearchResponse response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(KakaoBlogSearchResponse.class)
                .block();

        return response;
    }
}
