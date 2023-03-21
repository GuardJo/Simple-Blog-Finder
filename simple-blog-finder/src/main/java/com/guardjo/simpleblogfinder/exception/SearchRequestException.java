package com.guardjo.simpleblogfinder.exception;

public abstract class SearchRequestException extends IllegalArgumentException{
    public SearchRequestException(String s) {
        super(s);
    }
}
