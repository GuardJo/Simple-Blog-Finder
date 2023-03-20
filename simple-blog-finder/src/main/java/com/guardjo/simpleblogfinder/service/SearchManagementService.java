package com.guardjo.simpleblogfinder.service;

import com.guardjo.simpleblogfinder.dto.SearchTermDto;
import com.guardjo.simpleblogfinder.repository.SearchTermRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchManagementService {
    private final SearchTermRepository searchTermRepository;

    public SearchManagementService(SearchTermRepository searchTermRepository) {
        this.searchTermRepository = searchTermRepository;
    }

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
}
