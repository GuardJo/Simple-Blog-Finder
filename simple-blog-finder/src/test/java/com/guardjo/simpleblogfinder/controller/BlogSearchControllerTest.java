package com.guardjo.simpleblogfinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.guardjo.simpleblogfinder.constant.BlogSearchConstant;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchResponse;
import com.guardjo.simpleblogfinder.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WebMvcTest(BlogSearchController.class)
class BlogSearchControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private static KakaoBlogSearchResponse TEST_RESPONSE = TestDataGenerator.generateKakaoBlogSearchResponse();

    BlogSearchControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @DisplayName("블로그 검색 기본 테스트")
    @Test
    void testDefaultBlogSearch() throws Exception {
        String actualResponse = mockMvc.perform(MockMvcRequestBuilders.get(
                                BlogSearchConstant.REST_URL_PREFIX + BlogSearchConstant.REQUEST_BLOG_SEARCH_URL)
                        .queryParam("query", "test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expectedResponseString = objectMapper.writeValueAsString(TEST_RESPONSE);

        assertThat(actualResponse).isEqualTo(expectedResponseString);
    }

    @DisplayName("검색 값을 입력안했을 경우 테스트")
    @Test
    void testNullSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                        BlogSearchConstant.REST_URL_PREFIX + BlogSearchConstant.REQUEST_BLOG_SEARCH_URL))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("정렬 옵션 별 블로그 검색 테스트")
    @ParameterizedTest
    @ValueSource(strings = {BlogSearchConstant.SEARCH_SORT_TYPE_ACCURACY, BlogSearchConstant.SEARCH_SORT_TYPE_RECENCY})
    void testSortTypeBlogSearch(String blogSearchSortType) throws Exception {
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("query", "test");
        params.add("sort", blogSearchSortType);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders.get(
                                BlogSearchConstant.REST_URL_PREFIX + BlogSearchConstant.REQUEST_BLOG_SEARCH_URL)
                        .queryParams(params))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expectedResponseString = objectMapper.writeValueAsString(TEST_RESPONSE);

        assertThat(actualResponse).isEqualTo(expectedResponseString);
    }

    @DisplayName("블로그 검색 페이징 테스트")
    @Test
    void testPaginationSearchBlog() throws Exception {
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("query", "test");
        params.add("page", "0");
        params.add("size", "10");

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders.get(
                                BlogSearchConstant.REST_URL_PREFIX + BlogSearchConstant.REQUEST_BLOG_SEARCH_URL)
                        .queryParams(params))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expectedResponseString = objectMapper.writeValueAsString(TEST_RESPONSE);

        assertThat(actualResponse).isEqualTo(expectedResponseString);
    }
}