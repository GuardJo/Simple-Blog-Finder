package com.guardjo.simpleblogfinder.repository;

import com.guardjo.simpleblogfinder.domain.SearchTerm;
import com.guardjo.simpleblogfinder.dto.SearchTermDto;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class SearchTermRepositoryTest {
    private final static int TEST_DATA_SIZE = 10;
    private final static long TEST_DATA_MOST_COUNT = 94L;
    private final static long TEST_DATA_WORST_COUNT = 13L;
    @Autowired
    private SearchTermRepository searchTermRepository;

    @DisplayName("SearchTerm 단일 조회 테스트")
    @Test
    void tsetFindOneSearchTerm() {
        SearchTerm searchTerm = searchTermRepository.findById(1L).orElseThrow();

        assertThat(searchTerm.getSearchTermValue()).isEqualTo("Dannye Vasiljevic");
    }

    @DisplayName("전체 SearchTerm 조회 테스트")
    @Test
    void testFindALlSearchTerms() {
        int totalCount = (int) searchTermRepository.count();

        assertThat(totalCount).isEqualTo(TEST_DATA_SIZE);
    }

    @DisplayName("존재하지 않는 SearchTerm 조회 테스트")
    @Test
    void testNotFoundSearchTerm() {
        Optional<SearchTerm> findData = searchTermRepository.findById(0L);

        assertThat(findData.isEmpty()).isTrue();
    }

    @DisplayName("가장 많이 검색된 상위 10개의 SearchTerm 조회 테스트")
    @Test
    void testMostCountedSearchTermTop10() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "totalCount");
        Page<SearchTerm> searchTerms = searchTermRepository.findAll(pageable);

        List<SearchTerm> searchTermList = searchTerms.getContent();

        long topCount = searchTermList.get(0).getTotalCount();
        long bottomCount = searchTermList.get(searchTermList.size() - 1).getTotalCount();

        assertThat(topCount).isEqualTo(TEST_DATA_MOST_COUNT);
        assertThat(bottomCount).isEqualTo(TEST_DATA_WORST_COUNT);
    }

    @DisplayName("특정 검색어로 SearchTerm 조회 테스트")
    @Test
    void testFindSearchTermBySearchTermValue() {
        SearchTerm searchTerm = searchTermRepository.findSearchTermBySearchTermValueEqualsIgnoreCase("Dannye Vasiljevic").orElseThrow();

        assertThat(searchTerm)
                .hasFieldOrPropertyWithValue("searchTermValue", "Dannye Vasiljevic")
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @DisplayName("이미 저장되었던 검색어 중 일부 단어로 조회 테스트")
    @Test
    void testFindSearchTermBySearchTermContainingValue() {
        Optional<SearchTerm> searchTerm = searchTermRepository.findSearchTermBySearchTermValueEqualsIgnoreCase("Dannye");

        assertThat(searchTerm.isEmpty()).isTrue();
    }

    @DisplayName("SearchTerm Entity 객체 내 totalCount 갱신 테스트")
    @Test
    void testIncreaeCountSearchTerm() {
        SearchTerm searchTerm = searchTermRepository.findById(1L).orElseThrow();
        searchTerm.increaseCount();
        long expectedCount = searchTerm.getTotalCount();
        
        searchTermRepository.flush();

        SearchTerm afterSearchTerm = searchTermRepository.findById(1L).orElseThrow();
        long actualCount = afterSearchTerm.getTotalCount();

        assertThat(actualCount).isEqualTo(expectedCount);
    }
}