package com.guardjo.simpleblogfinder.controller;

import com.guardjo.simpleblogfinder.exception.KakaoRequestException;
import com.guardjo.simpleblogfinder.exception.SearchRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@ControllerAdvice(basePackageClasses = BlogSearchController.class)
public class BlogSearchExceptionHandler {
    @ExceptionHandler(SearchRequestException.class)
    public ResponseEntity<String> handleBlogSearchRequest(SearchRequestException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
