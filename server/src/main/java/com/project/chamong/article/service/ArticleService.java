package com.project.chamong.article.service;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.entity.Article;
import com.project.chamong.article.entity.ArticleLike;
import com.project.chamong.article.mapper.ArticleMapper;
import com.project.chamong.article.repository.ArticleLikeRepository;
import com.project.chamong.article.repository.ArticleRepository;
import com.project.chamong.article.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private static ArticleRepository articleRepository;
    private static ArticleLikeRepository articleLikeRepository;
    private static CommentRepository commentRepository;
    private static ArticleMapper articleMapper;

    public List<ArticleDto.Response> getAllArticles(){
        return articleRepository.findAll().stream()
                .map(articleMapper::articleResponse)
                .collect(Collectors.toList());
    }

    public ArticleDto.Response getArticle(Long id){
        Article article = articleRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Article not found ID: "+ id));
        increaseViewCnt(id);
        articleRepository.save(article);
        return articleMapper.articleResponse(article);
    }

    // Article 생성
    public ArticleDto.Response createArticle(ArticleDto.Post postDto) {
        Article article = articleMapper.articlePostDtoToArticle(postDto);
        article.setCreatedAt(LocalDateTime.now());
        article.setCreatedAt(LocalDateTime.now());
        article.setViewCnt(0);
        article.setLikeCnt(0);
        Article savedArticle = articleRepository.save(article);
        return articleMapper.articleResponse(savedArticle);
    }

    // Article 수정
//    public ArticleDto.Response updateArticle(Long id, ArticleDto.Patch patchDto) {
//        Article article = articleRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: "+ id));
//        Article updateArticle = articleMapper.articlePatchDtoToArticle(patchDto);
//        updateArticle.setId(article.getId());
//        updateArticle.setCreatedAt(article.getCreatedAt());
//        updateArticle.setUpdatedAt(LocalDateTime.now());
//        updateArticle.setTitle(article.getTitle());
//        updateArticle.setContent(article.getContent());
//        updateArticle.setArticleImg(article.getArticleImg());
//        updateArticle.setViewCnt(article.getViewCnt());
//        updateArticle.setLikeCnt(article.getLikeCnt());
//        updateArticle.setMemberId(article.getMemberId());
//        Article saveArticle = articleRepository.save(updateArticle);
//        return articleMapper.articleResponse(saveArticle);
//    }
    public ArticleDto.Response updateArticle(Long id, ArticleDto.Patch patchDto) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));
        article.update(articleMapper.articlePatchDtoToArticle(patchDto));
        Article updatedArticle = articleRepository.save(article);
        return articleMapper.articleResponse(updatedArticle);
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
        articleRepository.save(article);
    }
    @Transactional
    public void likeArticle(Long memberId, Long articleId) {
        ArticleLike articleLike = new ArticleLike();
        articleLike.setArticleId(articleId);
        articleLike.setMemberId(memberId);
        articleLikeRepository.save(articleLike);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        article.increaseLikeCnt();
        articleRepository.save(article);
    }

    @Transactional
    public void unlikeArticle(Long memberId, Long articleId) {
        ArticleLike articleLike = articleLikeRepository.findByMemberIdAndArticleId(memberId, articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article Like not found with articleId: " + articleId + " and memberId: " + memberId));
        articleLikeRepository.delete(articleLike);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        article.decreaseLikeCnt();
        articleRepository.save(article);
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

    public List<ArticleDto.Response> searchArticles(String keyword){
        List<Article> articles = articleRepository.findByTitleContaining(keyword);
        return articleMapper.articleResponseList(articles);
    }
}
