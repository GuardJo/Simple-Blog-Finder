package com.guardjo.simpleblogfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class KakaoBlogMetaData {
    private int totalCount;
    private int pageableCount;
    private boolean isEnd;
}
