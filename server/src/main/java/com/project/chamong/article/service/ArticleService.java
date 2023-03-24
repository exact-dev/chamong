package com.project.chamong.article.service;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.entity.Article;
import com.project.chamong.article.entity.Comment;
import com.project.chamong.article.mapper.ArticleMapper;
import com.project.chamong.article.repository.ArticleLikeRepository;
import com.project.chamong.article.repository.ArticleRepository;
import com.project.chamong.article.repository.CommentRepository;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final CommentRepository commentRepository;
    private final ArticleMapper articleMapper;
    private final MemberRepository memberRepository;
    
    public Page<ArticleDto.Response> getArticles(String keyword, Pageable pageable) {
        Page<Article> articlePage = StringUtils.isEmpty(keyword)
          ? articleRepository.findAll(pageable)
          : articleRepository.findByTitleContaining(keyword, pageable);
        
        Page<ArticleDto.Response> articleResponsePage = articlePage.map(articleMapper::toArticleResponse);
        
        for (ArticleDto.Response articleResponse : articleResponsePage.getContent()) {
            Article article = articleRepository.findById(articleResponse.getId())
              .orElseThrow(() -> new IllegalArgumentException("Article not found ID: " + articleResponse.getId()));
            Member member = article.getMember();
            articleResponse.setNickname(member.getNickname());
            articleResponse.setProfileImg(member.getProfileImg());
            articleResponse.setCarName(member.getCarName());
        }
        
        return articleResponsePage;
    }
    
    public ArticleDto.Response getArticle(Long id) {
        Article article = articleRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Article not found ID: " + id));
        
        Member member = article.getMember();
        increaseViewCnt(id);
        ArticleDto.Response response = articleMapper.toArticleResponse(article);
        response.setNickname(member.getNickname());
        response.setProfileImg(member.getProfileImg());
        response.setCarName(member.getCarName());
        return response;
    }
    // 인기글 조회 (5개씩, 1순위: 좋아요 수, 2순위: 조회수)
    public List<ArticleDto.Response> getPopularArticlesForWeb(){
        List<Article> popularArticles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCnt", "viewCnt"))
          .stream()
          .limit(5)
          .collect(Collectors.toList());
        
        return popularArticles.stream().map(articleMapper::toArticleResponse).collect(Collectors.toList());
    }
    // app 화면에서는 3개씩 조회
    public List<ArticleDto.Response> getPopularArticlesForApp(){
        List<Article> popularArticles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCnt", "viewCnt"))
          .stream()
          .limit(3)
          .collect(Collectors.toList());
        
        return popularArticles.stream().map(articleMapper::toArticleResponse).collect(Collectors.toList());
    }
    
    // Article 생성
    public ArticleDto.Response createArticle(ArticleDto.Post postDto) {
        Member member = memberRepository.findById(postDto.getMemberId())
          .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + postDto.getMemberId()));
        Article article = Article.createArticle(postDto, member);
        article.setMember(member);
        
        return articleMapper.toArticleResponse(articleRepository.save(article));
    }
    
    public ArticleDto.Response updateArticle(Long id, ArticleDto.Patch patchDto) {
        Article article = articleRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));
        
        article.update(patchDto);
        return articleMapper.toArticleResponse(article);
    }
    
    // Article 삭제
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));
        
        // 댓글 삭제
        for (Comment comment : article.getComments()) {
            commentRepository.delete(comment);
        }
        
        articleRepository.delete(article);
    }
    
    // 조회수
    public void increaseViewCnt(Long id) {
        Article article = articleRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Article not found ID:" + id));
        article.setViewCnt(article.getViewCnt() + 1);
    }

//    @Transactional
//    public void likeArticle(Long memberId, Long articleId) {
//        Article article = articleRepository.findById(articleId)
//                .orElseThrow(() -> new IllegalArgumentException("Article not found ID:" + articleId));
//        Member member = article.getMember();
//        ArticleLike articleLike = new ArticleLike();
//        articleLike.setArticle(article);
//        articleLike.setMember(member);
//        articleLikeRepository.save(articleLike);
//        article.increaseLikeCnt();
//    }
//
//    @Transactional
//    public void unlikeArticle(Long memberId, Long articleId) {
//        ArticleLike articleLike = articleLikeRepository.findByMemberIdAndArticleId(memberId, articleId)
//                .orElseThrow(() -> new IllegalArgumentException("Article Like not found with articleId: " + articleId + " and memberId: " + memberId));
//        articleLikeRepository.delete(articleLike);
//
//        Article article = articleRepository.findById(articleId)
//                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
//        article.decreaseLikeCnt();
//    }
    
    // 조회수 반환
    public int getArticleViewCnt(Long articleId) {
        Article article = articleRepository.findById(articleId)
          .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        return article.getViewCnt();
    }
    
    // 좋아요 수 반환
    public int getArticleLikeCnt(Long articleId) {
        Article article = articleRepository.findById(articleId)
          .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        return article.getLikeCnt();
    }
    
    // 댓글 수 반환
    public int getCommentCnt(Long articleId) {
        Article article = articleRepository.findById(articleId)
          .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        return article.getComments().size();
    }

}
