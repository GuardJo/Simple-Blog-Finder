package com.guardjo.simpleblogfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(staticName = "of")
public class KakaoBlogDocument {
    private String title;
    private String contents;
    private String url;
    private String blogname;
    private String thumbnail;
    private LocalDateTime dateTime;
}
