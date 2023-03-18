package com.guardjo.simpleblogfinder.util;

import com.guardjo.simpleblogfinder.dto.KakaoBlogDocument;
import com.guardjo.simpleblogfinder.dto.KakaoBlogMetaData;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchResponse;

import java.time.LocalDateTime;
import java.util.List;

public class TestDataGenerator {
    public static KakaoBlogMetaData generateKakaoBlogMetaData() {
        return KakaoBlogMetaData.of(
                5,
                5,
                true
        );
    }

    public static KakaoBlogDocument generateKakaoBlogDocument() {
        return KakaoBlogDocument.of(
                "작은 <b>집</b> <b>짓기</b> 기본컨셉 - <b>집</b><b>짓기</b> 초기구상하기",
                "이 점은 <b>집</b>을 지으면서 고민해보아야 한다. 하지만, 금액에 대한 가성비 대비 크게 문제되지 않을 부분이라 생각하여 설계로 극복하자고 생각했다. 전체 <b>집</b><b>짓기</b>의 기본방향은 크게 세 가지이다. 우선은 여가의 영역 증대이다. 현대 시대 일도 중요하지만, 여가시간 <b>집</b>에서 어떻게 보내느냐가 중요하니깐 이를 기본적...",
                "https://brunch.co.kr/@tourism/91",
                "정란수의 브런치",
                "http://search3.kakaocdn.net/argon/130x130_85_c/7r6ygzbvBDc",
                LocalDateTime.now()
        );
    }

    public static KakaoBlogSearchResponse generateKakaoBlogSearchResponse() {
        return KakaoBlogSearchResponse.of(
                generateKakaoBlogMetaData(),
                List.of(generateKakaoBlogDocument())
        );
    }
}
