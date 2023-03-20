package com.guardjo.simpleblogfinder.service;

import com.guardjo.simpleblogfinder.config.CommonConfig;
import com.guardjo.simpleblogfinder.domain.SearchTerm;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchResponse;
import com.guardjo.simpleblogfinder.dto.SearchTermDto;
import com.guardjo.simpleblogfinder.repository.SearchTermRepository;
import com.guardjo.simpleblogfinder.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@Import(CommonConfig.class)
@ExtendWith(MockitoExtension.class)
class BlogSearchServiceTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;
    @Mock
    private SearchTermRepository searchTermRepository;

    @InjectMocks
    private BlogSearchService blogSearchService;

    private final static KakaoBlogSearchResponse TEST_RESPONSE = TestDataGenerator.generateKakaoBlogSearchResponse();

    @DisplayName("블로그 검색 요청 테스트")
    @Test
    void testSearchBlog() {
        KakaoBlogSearchRequest kakaoBlogSearchRequest = KakaoBlogSearchRequest.of("test", null, 10, 1);

        given(webClient.get()
                .uri(any(URI.class))
                .retrieve()
                .bodyToMono(KakaoBlogSearchResponse.class)
                .block()).willReturn(TEST_RESPONSE);

        KakaoBlogSearchResponse actualResponse = blogSearchService.searchBlogs(kakaoBlogSearchRequest);

        assertThat(actualResponse).isEqualTo(TEST_RESPONSE);
    }

    @DisplayName("인기검색어 10종 반환 테스트")
    @Test
    void testSearchRanking() {
        List<SearchTermDto> expectedResponse = TestDataGenerator.generateSearchTermDtos();
        Page<SearchTerm> page = new PageImpl<>(TestDataGenerator.generateSearchTerms(), Pageable.ofSize(10), expectedResponse.size());

        given(searchTermRepository.findAll(any(Pageable.class))).willReturn(page);

        List<SearchTermDto> actualResponse = blogSearchService.findSearchTermRanking();

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
        then(searchTermRepository).should().findAll(any(Pageable.class));
    }

    @DisplayName("인기 검색어 총 개수가 10개 미만일 때 테스트")
    @Test
    void testSearchRankingNotSize10() {
        List<SearchTerm> dbResponse = List.of(
                TestDataGenerator.generateSearchTerm(3),
                TestDataGenerator.generateSearchTerm(2),
                TestDataGenerator.generateSearchTerm(1));
        List<SearchTermDto> expectedResponse = dbResponse.stream().map(SearchTermDto::from).collect(Collectors.toList());
        Page<SearchTerm> searchTermPage = new PageImpl<>(dbResponse, Pageable.ofSize(10), dbResponse.size());

        given(searchTermRepository.findAll(any(Pageable.class))).willReturn(searchTermPage);

        List<SearchTermDto> actualResponse = blogSearchService.findSearchTermRanking();

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
        assertThat(actualResponse.size()).isEqualTo(3);
        then(searchTermRepository).should().findAll(any(Pageable.class));
    }

    @DisplayName("기록된 검색어가 없을 경우 테스트")
    @Test
    void testNullSearchTermData() {
        given(searchTermRepository.findAll(any(Pageable.class))).willReturn(Page.empty());

        List<SearchTermDto> actualResponse = blogSearchService.findSearchTermRanking();

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(List.of());
        then(searchTermRepository).should().findAll(any(Pageable.class));
    }
}