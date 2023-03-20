package com.guardjo.simpleblogfinder.service;

import com.guardjo.simpleblogfinder.constant.BlogSearchConstant;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchResponse;
import com.guardjo.simpleblogfinder.dto.SearchTermDto;
import com.guardjo.simpleblogfinder.repository.SearchTermRepository;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BlogSearchService {
    private final WebClient webClient;
    private final SearchTermRepository searchTermRepository;

    public BlogSearchService(WebClient webClient, SearchTermRepository searchTermRepository) {
        this.webClient = webClient;
        this.searchTermRepository = searchTermRepository;
    }

    public KakaoBlogSearchResponse searchBlogs(KakaoBlogSearchRequest request) {
        URI uri = UriComponentsBuilder
                .fromUriString(BlogSearchConstant.KAKAO_BLOG_SEARCH_API_URL)
                .queryParam("query", request.getQuery())
                .queryParam("sort", request.getSort())
                .queryParam("page", request.getPage())
                .queryParam("size", request.getSize())
                .encode(CharsetUtil.UTF_8)
                .build()
                .toUri();

        KakaoBlogSearchResponse response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(KakaoBlogSearchResponse.class)
                .block();

        return response;
    }

    public List<SearchTermDto> findSearchTermRanking() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "totalCount");

        List<SearchTermDto> searchTermDtos = searchTermRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(SearchTermDto::from)
                .collect(Collectors.toList());

        return searchTermDtos;
    }
}
