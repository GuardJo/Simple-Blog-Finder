package com.guardjo.simpleblogfinder.constant;

public class BlogSearchConstant {
    // blog search sort type
    public final static String SEARCH_SORT_TYPE_ACCURACY = "accuracy";
    public final static String SEARCH_SORT_TYPE_RECENCY = "recency";

    // blog search urls
    public final static String REST_URL_PREFIX = "/api";
    public final static String REQUEST_BLOG_SEARCH_URL = "/blog-search";
    public final static String REQUEST_BLOG_SEARCH_KEYWORD_TOP_TEN = "/search-term/ranking";

    // kakao blog search api
    public final static String KAKAO_BLOG_SEARCH_API_URL = "https://dapi.kakao.com/v2/search/blog";
}
