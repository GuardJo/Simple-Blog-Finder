package com.guardjo.simpleblogfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class KakaoBlogSearchRequest {
    private String query;
    private String sort;
    private int page;
    private int size;
}
