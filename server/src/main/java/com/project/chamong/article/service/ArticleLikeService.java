package com.project.chamong.article.service;

import com.project.chamong.article.entity.Article;
import com.project.chamong.article.entity.ArticleLike;
import com.project.chamong.article.repository.ArticleLikeRepository;
import com.project.chamong.article.repository.ArticleRepository;
import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;
    
    @Transactional
    public void likeArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
          .orElseThrow(() -> new IllegalArgumentException("Article not found ID:" + articleId));
        
        ArticleLike articleLike = new ArticleLike();
        Member member = article.getMember();
        articleLike.setArticle(articleRepository.getOne(articleId));
        articleLike.setMember(member);
        articleLikeRepository.save(articleLike);
        
        article.increaseLikeCnt();
    }
    
    
    @Transactional
    public void unlikeArticle(AuthorizedMemberDto authorizedMemberDto, Long articleId) {
        ArticleLike articleLike = articleLikeRepository.findByMemberIdAndArticleId(articleId, authorizedMemberDto.getId())
          .orElseThrow(() -> new IllegalArgumentException("Article Like not found with articleId: " + articleId + " and memberId: " + authorizedMemberDto.getId()));
        articleLikeRepository.delete(articleLike);
        
        Article article = articleRepository.findById(articleId)
          .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        article.decreaseLikeCnt();
    }

}
