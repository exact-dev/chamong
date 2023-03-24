package com.project.chamong.article.controller;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.service.ArticleLikeService;
import com.project.chamong.article.service.ArticleService;
import com.project.chamong.auth.dto.AuthorizedMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleLikeService articleLikeService;
    
    // 인기글 보여주기 - web
    @GetMapping("/articles/popular-web")
    public ResponseEntity<List<ArticleDto.Response>> getPopularArticlesForWeb(){
        List<ArticleDto.Response> popularArticles = articleService.getPopularArticlesForWeb();
        return ResponseEntity.ok(popularArticles);
    }
    
    // 인기글 보여주기 - app
    @GetMapping("/articles/popular-app")
    public ResponseEntity<List<ArticleDto.Response>> getPopularArticlesForApp(){
        List<ArticleDto.Response> popularArticles = articleService.getPopularArticlesForApp();
        return ResponseEntity.ok(popularArticles);
    }
    // 15개씩 보여주기
    @GetMapping("/articles")
    public ResponseEntity<Page<ArticleDto.Response>> getAllArticles(@RequestParam(value = "keyword", required = false) String keyword,
                                                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "size", defaultValue = "15") int size) {
        // 신규 등록 순으로 정렬
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ArticleDto.Response> articles = articleService.getArticles(keyword, pageRequest);
        return ResponseEntity.ok(articles);
    }
    
    // 특정 게시글 보이기
    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleDto.Response> getArticle(@PathVariable Long id) {
        ArticleDto.Response article = articleService.getArticle(id);
        article.setViewCnt(articleService.getArticleViewCnt(id));
        article.setCommentCnt(articleService.getCommentCnt(id));
        article.setLikeCnt(articleService.getArticleLikeCnt(id));
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
    public ResponseEntity<Void> likeArticle(@PathVariable Long id) {
        articleLikeService.likeArticle(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/articles/{id}/unlike")
    public ResponseEntity<Void> unlikeArticle(@PathVariable Long id, @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto) {
        articleLikeService.unlikeArticle(authorizedMemberDto, id);
        return ResponseEntity.ok().build();
    }
}
