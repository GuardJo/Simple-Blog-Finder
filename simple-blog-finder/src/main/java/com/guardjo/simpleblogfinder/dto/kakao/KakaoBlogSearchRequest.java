package com.guardjo.simpleblogfinder.dto.kakao;

import com.guardjo.simpleblogfinder.dto.BlogSearchRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class KakaoBlogSearchRequest extends BlogSearchRequest {
    private String query;
    private String sort;
    private int page;
    private int size;
}
