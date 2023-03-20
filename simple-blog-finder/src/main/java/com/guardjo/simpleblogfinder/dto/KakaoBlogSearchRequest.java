package com.guardjo.simpleblogfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class KakaoBlogSearchRequest {
    private String query;
    private String sort;
    private int page;
    private int size;
}
