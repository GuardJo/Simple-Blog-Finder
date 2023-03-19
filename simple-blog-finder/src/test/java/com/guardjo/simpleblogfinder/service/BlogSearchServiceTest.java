package com.guardjo.simpleblogfinder.service;

import com.guardjo.simpleblogfinder.config.CommonConfig;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchResponse;
import com.guardjo.simpleblogfinder.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Import(CommonConfig.class)
@ExtendWith(MockitoExtension.class)
class BlogSearchServiceTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

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
}