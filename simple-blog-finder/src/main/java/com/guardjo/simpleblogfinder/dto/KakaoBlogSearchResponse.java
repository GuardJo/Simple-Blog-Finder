package com.guardjo.simpleblogfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoBlogSearchResponse {
    private KakaoBlogMetaData meta;
    private List<KakaoBlogDocument> documents = new ArrayList<>();
}
