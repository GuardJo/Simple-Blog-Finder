package com.guardjo.simpleblogfinder.controller;

import com.guardjo.simpleblogfinder.constant.BlogSearchConstant;
import com.guardjo.simpleblogfinder.dto.BlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.BlogSearchResponse;
import com.guardjo.simpleblogfinder.dto.SearchTermDto;
import com.guardjo.simpleblogfinder.dto.kakao.KakaoBlogSearchRequest;
import com.guardjo.simpleblogfinder.dto.kakao.KakaoBlogSearchResponse;
import com.guardjo.simpleblogfinder.service.BlogSearchService;
import com.guardjo.simpleblogfinder.service.SearchManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Tag(name = "블로그 검색 관련 API", description = "검색어를 기반으로한 검색정보 반환 및 인기 검색어 조회가 가능하다.")
@RestController
@RequestMapping(BlogSearchConstant.REST_URL_PREFIX)
@Slf4j
public class BlogSearchController {
    private final BlogSearchService blogSearchService;
    private final SearchManagementService searchManagementService;

    public BlogSearchController(@Autowired BlogSearchService blogSearchService,
                                @Autowired SearchManagementService searchManagementService) {
        this.blogSearchService = blogSearchService;
        this.searchManagementService = searchManagementService;
    }

    @Operation(summary = "검색 결과 반환", description = "검색어를 입력하여 검색 정보 반환")
    @Parameters(value = {
            @Parameter(name = "searchValue", description = "검색어"),
            @Parameter(name = "searchApiType", description = "검색 사이트 종류"),
            @Parameter(name = "sort", description = "정렬 종류 : accuracy(정확도순), recency(최신순)"),
            @Parameter(name = "size", description = "반환 받는 한 페이지의 크기"),
            @Parameter(name = "page", description = "반환 받는 목록의 페이지 index")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카카오 블로그 검색 조회 결과",
            content = @Content(schema = @Schema(implementation = KakaoBlogSearchResponse.class))),
            @ApiResponse(responseCode = "400", description = "검색 요청 데이터 오류",
                    content = @Content(schema = @Schema(implementation = HttpServletResponse.class)))
    })
    @Cacheable
    @GetMapping(BlogSearchConstant.REQUEST_BLOG_SEARCH_URL)
    public BlogSearchResponse searchBlog(@RequestParam(defaultValue = BlogSearchConstant.KAKAO_API) String searchApiType,
                                         @RequestParam String searchValue,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = BlogSearchConstant.SEARCH_SORT_TYPE_ACCURACY) String sort,
                                         @RequestParam(defaultValue = "1") int page) throws Exception {
        log.info("[Test] Request Search Blogs, searchValue = {}, sortType = {}, pageSize = {}, pageNumber= {}",
                searchValue, sort, size, page);

        BlogSearchRequest request = null;
        BlogSearchResponse response = null;

        // TODO 향후 다른 블로그 검색 API 호출 기능 추가 시 관련 작업 추가 (default = KAKAO)
        switch (searchApiType) {
            default:
                request = KakaoBlogSearchRequest.of(searchValue, sort, page, size);
                response = blogSearchService.searchBlogs((KakaoBlogSearchRequest) request);
        }

        searchManagementService.countSearchTerm(searchValue);

        return response;
    }

    @Operation(summary = "인기 검색어 조회", description = "그간 검색된 검색어 중 상위 10위권 내 검색어 목록 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인기 검색어 목록 반환",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SearchTermDto.class))))
    })
    @GetMapping(BlogSearchConstant.REQUEST_BLOG_SEARCH_KEYWORD_TOP_TEN)
    public List<SearchTermDto> findSearchTermRanking() {
        log.info("[Test] Request SearchTerm TOP10 Ranking");

        return searchManagementService.findSearchTermRanking();
    }
}
