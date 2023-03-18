package com.guardjo.simpleblogfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class KakaoBlogSearchResponse {
    private KakaoBlogMetaData meta;
    private List<KakaoBlogDocument> documents = new ArrayList<>();
}
