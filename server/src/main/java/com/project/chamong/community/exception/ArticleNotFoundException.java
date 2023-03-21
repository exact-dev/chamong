package com.project.chamong.community.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 커뮤니티를 찾지 못할 경우 발생하는 예외 클래스
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArticleNotFoundException extends RuntimeException{
    public ArticleNotFoundException(Long id){
        super("Community not found: "+ id);
    }
}
