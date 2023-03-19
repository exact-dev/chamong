package com.project.chamong.article.controller;

import com.project.chamong.article.dto.ArticleLikeDto;
import com.project.chamong.article.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ArticleLikeController {
    private final ArticleLikeService articleLikeService;

    // 좋아요 추가
    @PostMapping
    public ResponseEntity<Void> likeArticle(@PathVariable Long id,@RequestBody ArticleLikeDto.Post postDto){
        articleLikeService.likeArticle(id, postDto.getMemberId());
        return ResponseEntity.ok().build();
    }

    // 좋아요 삭제
    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> unlikeArticle(@PathVariable Long articleId, @RequestParam Long memberId) {
        articleLikeService.unlikeArticle(articleId,memberId);
        return ResponseEntity.noContent().build();
    }
}
