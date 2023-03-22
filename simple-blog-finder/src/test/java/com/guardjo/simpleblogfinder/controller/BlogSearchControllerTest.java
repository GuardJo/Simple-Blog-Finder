package com.guardjo.simpleblogfinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardjo.simpleblogfinder.config.TestConfig;
import com.guardjo.simpleblogfinder.constant.BlogSearchConstant;
import com.guardjo.simpleblogfinder.dto.kakao.KakaoBlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.kakao.KakaoBlogSearchResponse;
import com.guardjo.simpleblogfinder.dto.SearchTermDto;
import com.guardjo.simpleblogfinder.exception.KakaoRequestException;
import com.guardjo.simpleblogfinder.service.BlogSearchService;
import com.guardjo.simpleblogfinder.service.SearchManagementService;
import com.guardjo.simpleblogfinder.util.kakaoBlogSearchRequestChecker;
import com.guardjo.simpleblogfinder.util.TestDataGenerator;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestConfig.class)
@WebMvcTest(BlogSearchController.class)
class BlogSearchControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private BlogSearchService blogSearchService;
    @MockBean
    private kakaoBlogSearchRequestChecker kakaoBlogSearchRequestChecker;
    @MockBean
    private SearchManagementService searchManagementService;

    private static KakaoBlogSearchResponse TEST_RESPONSE = TestDataGenerator.generateKakaoBlogSearchResponse();
    private static SearchTermDto TEST_SEARCH_TERM_DTO = SearchTermDto.from(TestDataGenerator.generateSearchTerm(1L));

    BlogSearchControllerTest(@Autowired MockMvc mockMvc, @Autowired ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @DisplayName("블로그 검색 기본 테스트")
    @Test
    void testDefaultBlogSearch() throws Exception {
        given(blogSearchService.searchBlogs(any(KakaoBlogSearchRequest.class))).willReturn(TEST_RESPONSE);
        willDoNothing().given(searchManagementService).countSearchTerm(anyString());

        String actualResponse = mockMvc.perform(get(
                        BlogSearchConstant.REST_URL_PREFIX + BlogSearchConstant.REQUEST_BLOG_SEARCH_URL)
                        .queryParam("searchValue", "test"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(CharsetUtil.UTF_8);

        String expectedResponseString = objectMapper.writeValueAsString(TEST_RESPONSE);

        then(blogSearchService).should().searchBlogs(any(KakaoBlogSearchRequest.class));
        then(searchManagementService).should().countSearchTerm(anyString());
        assertThat(actualResponse).isEqualTo(expectedResponseString);
    }

    @DisplayName("검색 값을 입력안했을 경우 테스트")
    @Test
    void testNullSearch() throws Exception {
        given(blogSearchService.searchBlogs(any(KakaoBlogSearchRequest.class))).willReturn(TEST_RESPONSE);

        mockMvc.perform(get(
                        BlogSearchConstant.REST_URL_PREFIX + BlogSearchConstant.REQUEST_BLOG_SEARCH_URL))
                .andExpect(status().isBadRequest());

        then(blogSearchService).shouldHaveNoInteractions();
    }

    @DisplayName("요청 인자의 범위를 넘어섰을 경우 테스트 (카카오)")
    @ParameterizedTest
    @MethodSource("getParameterTestData")
    void testBadRequestSearch(String paramKey, String paramValue) throws Exception {
        given(blogSearchService.searchBlogs(any(KakaoBlogSearchRequest.class))).willThrow(new KakaoRequestException("test"));

        MultiValueMap params = new LinkedMultiValueMap();
        params.add("searchValue", "test");
        params.add(paramKey, paramValue);

        mockMvc.perform(get(
                        BlogSearchConstant.REST_URL_PREFIX + BlogSearchConstant.REQUEST_BLOG_SEARCH_URL)
                        .queryParams(params))
                .andExpect(status().isBadRequest());

        then(blogSearchService).should().searchBlogs(any(KakaoBlogSearchRequest.class));
    }

    @DisplayName("정렬 옵션 별 블로그 검색 테스트")
    @ParameterizedTest
    @ValueSource(strings = {BlogSearchConstant.SEARCH_SORT_TYPE_ACCURACY, BlogSearchConstant.SEARCH_SORT_TYPE_RECENCY})
    void testSortTypeBlogSearch(String blogSearchSortType) throws Exception {
        given(blogSearchService.searchBlogs(any(KakaoBlogSearchRequest.class))).willReturn(TEST_RESPONSE);
        willDoNothing().given(searchManagementService).countSearchTerm(anyString());

        MultiValueMap params = new LinkedMultiValueMap();
        params.add("searchValue", "test");
        params.add("sort", blogSearchSortType);

        String actualResponse = mockMvc.perform(get(
                        BlogSearchConstant.REST_URL_PREFIX + BlogSearchConstant.REQUEST_BLOG_SEARCH_URL)
                        .queryParams(params))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(CharsetUtil.UTF_8);

        String expectedResponseString = objectMapper.writeValueAsString(TEST_RESPONSE);

        then(blogSearchService).should().searchBlogs(any(KakaoBlogSearchRequest.class));
        then(searchManagementService).should().countSearchTerm(anyString());
        assertThat(actualResponse).isEqualTo(expectedResponseString);
    }

    @DisplayName("블로그 검색 페이징 테스트")
    @Test
    void testPaginationSearchBlog() throws Exception {
        given(blogSearchService.searchBlogs(any(KakaoBlogSearchRequest.class))).willReturn(TEST_RESPONSE);
        willDoNothing().given(searchManagementService).countSearchTerm(anyString());

        MultiValueMap params = new LinkedMultiValueMap();
        params.add("searchValue", "test");
        params.add("page", "0");
        params.add("size", "10");

        String actualResponse = mockMvc.perform(get(
                        BlogSearchConstant.REST_URL_PREFIX + BlogSearchConstant.REQUEST_BLOG_SEARCH_URL)
                        .queryParams(params))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(CharsetUtil.UTF_8);

        String expectedResponseString = objectMapper.writeValueAsString(TEST_RESPONSE);

        then(blogSearchService).should().searchBlogs(any(KakaoBlogSearchRequest.class));
        then(searchManagementService).should().countSearchTerm(anyString());
        assertThat(actualResponse).isEqualTo(expectedResponseString);
    }

    @DisplayName("인기검색어 랭킹 반환 테스트")
    @Test
    void testSearchTermRanking() throws Exception {
        List<SearchTermDto> expectedResponse = TestDataGenerator.generateSearchTermDtos();
        given(searchManagementService.findSearchTermRanking()).willReturn(expectedResponse);

        String actualResponse = mockMvc.perform(get(
                BlogSearchConstant.REST_URL_PREFIX + BlogSearchConstant.REQUEST_BLOG_SEARCH_KEYWORD_TOP_TEN))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(CharsetUtil.UTF_8);

        String expectedResponseString = objectMapper.writeValueAsString(expectedResponse);

        then(searchManagementService).should().findSearchTermRanking();
        assertThat(actualResponse).isEqualTo(expectedResponseString);
    }

    private static Stream<Arguments> getParameterTestData() {
        return Stream.of(
                arguments("page", "0"),
                arguments("page", "55"),
                arguments("size", "0"),
                arguments("size", "550")
        );
    }
}