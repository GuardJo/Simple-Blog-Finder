package com.guardjo.simpleblogfinder.dto;

import com.guardjo.simpleblogfinder.domain.SearchTerm;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class SearchTermDto {
    private String searchTermValue;
    private long totalCount;

    public static SearchTermDto from(SearchTerm searchTerm) {
        return new SearchTermDto(searchTerm.getSearchTermValue(), searchTerm.getTotalCount());
    }
}
