package com.project.chamong.article.service;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.entity.Article;
import com.project.chamong.article.entity.ArticleLike;
import com.project.chamong.article.mapper.ArticleMapper;
import com.project.chamong.article.repository.ArticleLikeRepository;
import com.project.chamong.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleMapper articleMapper;

    public List<ArticleDto.Response> getArticles(String keyword) {
        return StringUtils.isEmpty(keyword)
                ? articleRepository.findAll().stream().map(articleMapper::articleResponse).collect(Collectors.toList())
                : articleRepository.findByTitleContaining(keyword).stream().map(articleMapper::articleResponse).collect(Collectors.toList());
    }

    public ArticleDto.Response getArticle(Long id){
        Article article = articleRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Article not found ID: "+ id));
        increaseViewCnt(id);
        return articleMapper.articleResponse(article);
    }

    // Article 생성
    public ArticleDto.Response createArticle(ArticleDto.Post postDto) {
        Article article = articleRepository.save(Article.createArticle(postDto));
        return articleMapper.articleResponse(article);
    }

    public ArticleDto.Response updateArticle(Long id, ArticleDto.Patch patchDto) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));

        article.update(patchDto);
        return articleMapper.articleResponse(article);
    }

    // Article 삭제
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: "+ id));

        articleRepository.delete(article);
    }

    // 조회수
    public void increaseViewCnt(Long id) {
        Article article = articleRepository.findById(id)
                        .orElseThrow(()-> new IllegalArgumentException("Article not found ID:"+id));
        article.setViewCnt(article.getViewCnt() + 1);
    }
    @Transactional
    public void likeArticle(Long memberId, Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()-> new IllegalArgumentException("Article not found ID:"+articleId));
        ArticleLike articleLike = new ArticleLike();
        articleLike.setArticle(article);
        articleLike.setMemberId(memberId);
        articleLikeRepository.save(articleLike);
        article.increaseLikeCnt();
    }

    @Transactional
    public void unlikeArticle(Long memberId, Long articleId) {
        ArticleLike articleLike = articleLikeRepository.findByMemberIdAndArticleId(memberId, articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article Like not found with articleId: " + articleId + " and memberId: " + memberId));
        articleLikeRepository.delete(articleLike);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        article.decreaseLikeCnt();
    }
    public int getArticleViewCnt(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        return article.getViewCnt();
    }

    public int getArticleLikeCnt(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        return article.getLikeCnt();
    }

    public int getCommentCnt(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        return article.getComments().size();
    }
}
