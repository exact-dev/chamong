package com.project.chamong.community.controller;

import com.project.chamong.community.mapper.ArticleMapper;
import com.project.chamong.community.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/communities")
public class CommentController {
    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @Autowired
    public CommentController(ArticleService articleService, ArticleMapper articleMapper){
        this.articleMapper = articleMapper;
        this.articleService = articleService;
    }

    // 댓글 목록 조회

    // 댓글 생성
    // 댓글 수정
    // 댁글 삭제


}
