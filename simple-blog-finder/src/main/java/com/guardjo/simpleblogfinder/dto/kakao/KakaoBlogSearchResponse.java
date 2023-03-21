package com.guardjo.simpleblogfinder.dto.kakao;

import com.guardjo.simpleblogfinder.dto.BlogSearchResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoBlogSearchResponse extends BlogSearchResponse {
    private KakaoBlogMetaData meta;
    private List<KakaoBlogDocument> documents = new ArrayList<>();
}
