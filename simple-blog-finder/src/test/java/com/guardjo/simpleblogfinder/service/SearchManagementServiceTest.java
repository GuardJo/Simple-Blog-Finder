package com.guardjo.simpleblogfinder.service;

import com.guardjo.simpleblogfinder.domain.SearchTerm;
import com.guardjo.simpleblogfinder.dto.SearchTermDto;
import com.guardjo.simpleblogfinder.repository.SearchTermRepository;
import com.guardjo.simpleblogfinder.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SearchManagementServiceTest {
    @Mock
    private SearchTermRepository searchTermRepository;
    @InjectMocks
    private SearchManagementService searchManagementService;

    @DisplayName("인기검색어 10종 반환 테스트")
    @Test
    void testSearchRanking() {
        List<SearchTermDto> expectedResponse = TestDataGenerator.generateSearchTermDtos();
        Page<SearchTerm> page = new PageImpl<>(TestDataGenerator.generateSearchTerms(), Pageable.ofSize(10), expectedResponse.size());

        given(searchTermRepository.findAll(any(Pageable.class))).willReturn(page);

        List<SearchTermDto> actualResponse = searchManagementService.findSearchTermRanking();

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

        List<SearchTermDto> actualResponse = searchManagementService.findSearchTermRanking();

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
        assertThat(actualResponse.size()).isEqualTo(3);
        then(searchTermRepository).should().findAll(any(Pageable.class));
    }

    @DisplayName("기록된 검색어가 없을 경우 테스트")
    @Test
    void testNullSearchTermData() {
        given(searchTermRepository.findAll(any(Pageable.class))).willReturn(Page.empty());

        List<SearchTermDto> actualResponse = searchManagementService.findSearchTermRanking();

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(List.of());
        then(searchTermRepository).should().findAll(any(Pageable.class));
    }

    @DisplayName("주어진 검색어에 해당하는 SearchTermDto 객체가 있을 때 반환 테스트")
    @Test
    void testFoundSearchTermBySearchValue() {
        SearchTerm expectedData = TestDataGenerator.generateSearchTerm(10L);
        given(searchTermRepository.findSearchTermBySearchTermValueEqualsIgnoreCase(anyString()))
                .willReturn(Optional.of(expectedData));

        SearchTermDto actualData = searchManagementService.findSearchTerm("test");

        assertThat(actualData).isEqualTo(SearchTermDto.from(expectedData));
        then(searchTermRepository).should().findSearchTermBySearchTermValueEqualsIgnoreCase(anyString());
        then(searchTermRepository).shouldHaveNoMoreInteractions();
    }

    @DisplayName("주어진 검색어에 해당하는 SearchTerm을 찾지 못했을 때 (신규 생성함) 반환 테스트")
    @Test
    void testNotFoundSearchTermBySearchValue() {
        SearchTerm expectedData = TestDataGenerator.generateSearchTerm(10L);
        given(searchTermRepository.findSearchTermBySearchTermValueEqualsIgnoreCase(anyString()))
                .willReturn(Optional.empty());
        given(searchTermRepository.save(any(SearchTerm.class))).willReturn(expectedData);

        SearchTermDto actualData = searchManagementService.findSearchTerm("test");

        assertThat(actualData).isEqualTo(SearchTermDto.from(expectedData));
        then(searchTermRepository).should().findSearchTermBySearchTermValueEqualsIgnoreCase(anyString());
        then(searchTermRepository).should().save(any(SearchTerm.class));
    }
}