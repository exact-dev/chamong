package com.project.chamong.article.service;

import com.project.chamong.article.dto.ArticleLikeDto;
import com.project.chamong.article.entity.Article;
import com.project.chamong.article.entity.ArticleLike;
import com.project.chamong.article.mapper.ArticleLikeMapper;
import com.project.chamong.article.repository.ArticleLikeRepository;
import com.project.chamong.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeMapper articleLikeMapper;
    private final ArticleRepository articleRepository;

//    public void likeArticle(ArticleLikeDto.Post postDto){
//        ArticleLike articleLike = articleLikeMapper.articleLikePostToarticleLike(postDto);
//        articleLikeRepository.save(articleLike);
//    }
//    @Transactional
//    public void unlikeArticle(Long articleId){
//        ArticleLike articleLike = articleLikeRepository.findByMemberIdAndArticleId(memberId, articleId)
//                .orElseThrow(()-> new IllegalArgumentException("Article Like not found with articleId: "+ articleId));
//        articleLikeRepository.delete(articleLike);
//
//    }

    @Transactional
    public void likeArticle(Long articleId, Long memberId) {
        ArticleLike articleLike = new ArticleLike();
        articleLike.setArticle(articleRepository.getOne(articleId));
        articleLike.setMemberId(memberId);
        articleLikeRepository.save(articleLike);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        article.increaseLikeCnt();
    }

    @Transactional
    public void unlikeArticle(Long memberId, Long articleId) {
        ArticleLike articleLike = articleLikeRepository.findByMemberIdAndArticleId(articleId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("Article Like not found with articleId: " + articleId + " and memberId: " + memberId));
        articleLikeRepository.delete(articleLike);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        article.decreaseLikeCnt();
    }
    // 게시물 고유 번호를 받아 해당 게시물의 좋아요 개수 반환
    public long countLikesByArticle(Long articleId){
        return articleLikeRepository.countByArticleId(articleId);
    }

}
