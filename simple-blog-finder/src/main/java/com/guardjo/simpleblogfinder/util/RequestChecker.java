package com.guardjo.simpleblogfinder.util;

import com.guardjo.simpleblogfinder.constant.BlogSearchConstant;
import com.guardjo.simpleblogfinder.dto.KakaoBlogSearchRequest;
import com.guardjo.simpleblogfinder.exception.KakaoRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RequestChecker {
    private final static int MAX_COUNT = 50;
    private final static int MIN_COUNT = 1;

    public boolean blogSearchRequestValidate(KakaoBlogSearchRequest request) throws KakaoRequestException{
        return validateToSearchValue(request) &&
                validateToSort(request) &&
                validatePagination(request.getPage()) &&
                validatePagination(request.getSize());
    }

    private boolean validateToSearchValue(KakaoBlogSearchRequest request) throws KakaoRequestException{
        if (request.getQuery().isBlank() || request.getQuery().isEmpty()) {
            log.error("[Test] searchValue is Null Or Empty, Required searchValue");
            throw new KakaoRequestException("searchValue is Null Or Empty, Required searchValue");
        } else {
            return true;
        }
    }

    // sort 타입이 원하는 값이 아니게 전달해도 값 출력에는 문제 없음
    private boolean validateToSort(KakaoBlogSearchRequest request) {
        if (!(request.getSort().equals(BlogSearchConstant.SEARCH_SORT_TYPE_ACCURACY)
                || request.getSort().equals(BlogSearchConstant.SEARCH_SORT_TYPE_RECENCY))) {
            log.warn("[Test] sort is Wrong value, current sort = {}", request.getSort());
        }

        return true;
    }

    private boolean validatePagination(int pageSizeOrPageNumber) throws KakaoRequestException{
        if (pageSizeOrPageNumber <= MAX_COUNT && pageSizeOrPageNumber >= MIN_COUNT) {
            return true;
        } else {
            throw new KakaoRequestException("Out of bind Page or Size, Page/Size Max is 50, Min is 1");
        }
    }
}
