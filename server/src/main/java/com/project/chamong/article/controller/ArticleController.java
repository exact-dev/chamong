package com.project.chamong.article.controller;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.dto.ArticleLikeDto;
import com.project.chamong.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communities")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleDto.Response>> getAllArticles() {
        List<ArticleDto.Response> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto.Response> getArticle(@PathVariable Long id) {
        ArticleDto.Response article = articleService.getArticle(id);
        return ResponseEntity.ok(article);
    }

    @PostMapping
    public ResponseEntity<ArticleDto.Response> createArticle(@RequestBody ArticleDto.Post postDto) {
        ArticleDto.Response response = articleService.createArticle(postDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ArticleDto.Response> updateArticle(@PathVariable Long id, @RequestBody ArticleDto.Patch patchDto) {
        ArticleDto.Response response = articleService.updateArticle(id, patchDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeArticle(@PathVariable Long id, @RequestBody Long memberId) {
        articleService.likeArticle(id, memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/unlike")
    public ResponseEntity<Void> unlikeArticle(@PathVariable Long id, @RequestBody Long memberId){
        articleService.unlikeArticle(id, memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArticleDto.Response>> searchArticles(@RequestParam("q") String keyword){
        List<ArticleDto.Response> articles = articleService.searchArticles(keyword);
        return ResponseEntity.ok(articles);
    }
}
