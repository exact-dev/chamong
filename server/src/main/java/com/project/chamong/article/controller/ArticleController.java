package com.project.chamong.article.controller;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDto.Response>> getAllArticles(@RequestParam(value = "keyword", required = false) String keyword) {
        List<ArticleDto.Response> articles = articleService.getArticles(keyword);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleDto.Response> getArticle(@PathVariable Long id) {
        ArticleDto.Response article = articleService.getArticle(id);
        return ResponseEntity.ok(article);
    }

    @PostMapping("/articles")
    public ResponseEntity<ArticleDto.Response> createArticle(@RequestBody ArticleDto.Post postDto) {
        ArticleDto.Response response = articleService.createArticle(postDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/articles/{id}")
    public ResponseEntity<ArticleDto.Response> updateArticle(@PathVariable Long id, @RequestBody ArticleDto.Patch patchDto) {
        ArticleDto.Response response = articleService.updateArticle(id, patchDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/articles/{id}/like")
    public ResponseEntity<Void> likeArticle(@PathVariable Long id, @RequestBody Long memberId) {
        articleService.likeArticle(id, memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/articles/{id}/unlike")
    public ResponseEntity<Void> unlikeArticle(@PathVariable Long id, @RequestBody Long memberId){
        articleService.unlikeArticle(id, memberId);
        return ResponseEntity.ok().build();
    }
}
