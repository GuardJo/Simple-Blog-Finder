package com.guardjo.simpleblogfinder.service;

import com.guardjo.simpleblogfinder.domain.SearchTerm;
import com.guardjo.simpleblogfinder.dto.SearchTermDto;
import com.guardjo.simpleblogfinder.repository.SearchTermRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class SearchManagementService {
    private final SearchTermRepository searchTermRepository;

    public SearchManagementService(SearchTermRepository searchTermRepository) {
        this.searchTermRepository = searchTermRepository;
    }

    @Transactional(readOnly = true)
    public List<SearchTermDto> findSearchTermRanking() {
        log.info("[Test] Most 10 Populate SearchTerm, Calculating...");
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "totalCount");

        List<SearchTermDto> searchTermDtos = searchTermRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(SearchTermDto::from)
                .collect(Collectors.toList());

        log.info("[Test] Caculated Most 10 Populate SearchTerms!");
        return searchTermDtos;
    }

    public void countSearchTerm(String searchValue) {

        Optional<SearchTerm> searchTerm = searchTermRepository.findSearchTermBySearchTermValueEqualsIgnoreCase(searchValue);

        if (searchTerm.isEmpty()) {
            saveSearchTerm(searchValue);
        } else {
            searchTerm.get().increaseCount();
        }

        log.info("[Test] Counted SearchTerm, searchTermValue = {}", searchValue);
    }

    private SearchTerm saveSearchTerm(String searchValue) {
        SearchTerm searchTerm = searchTermRepository.save(SearchTerm.of(null, searchValue, 1L));

        log.info("[Test] Save SearchTerm, {}", searchTerm.toString());

        return searchTerm;
    }
}
