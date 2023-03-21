package com.guardjo.simpleblogfinder.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoBlogDocument {
    private String title;
    private String contents;
    private String url;
    private String blogname;
    private String thumbnail;
    private ZonedDateTime datetime;
}
