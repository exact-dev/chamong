package com.project.chamong.article.service;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.entity.Article;
import com.project.chamong.article.entity.Comment;
import com.project.chamong.article.mapper.ArticleMapper;
import com.project.chamong.article.repository.ArticleLikeRepository;
import com.project.chamong.article.repository.ArticleRepository;
import com.project.chamong.article.repository.CommentRepository;
import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final ArticleMapper articleMapper;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    
    // 게시글 전체 조회
    @Transactional
    public Page<ArticleDto.Response> getArticles(String keyword, Pageable pageable) {
        Page<Article> articlePage = StringUtils.isEmpty(keyword)
          ? articleRepository.findAll(pageable)
          : articleRepository.findByTitleContaining(keyword, pageable);
        
        Page<ArticleDto.Response> articleResponsePage = articlePage.map(articleMapper::articleResponse);
        
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
    
    // 상세페이지 - 게시글과 댓글 조회
    @Transactional
    public ArticleDto.Response getArticle(Long id, AuthorizedMemberDto authorizedMemberDto) {
        Article article = articleRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Article not found ID: " + id));
        
        Member findMember = memberService.findByEmail(authorizedMemberDto.getEmail());
        increaseViewCnt(id);
        
        ArticleDto.Response response = articleMapper.articleResponse(article, findMember);
        
        return response;
    }
    
    // 인기글 조회 (5개씩, 1순위: 좋아요 수, 2순위: 조회수)
    public List<ArticleDto.Response> getPopularArticlesForWeb(){
        List<Article> popularArticles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCnt", "viewCnt")).stream().limit(5).collect(Collectors.toList());
        return popularArticles.stream().map(articleMapper::articleResponse).collect(Collectors.toList());
    }
    
    // app 화면에서는 3개씩 조회
    public List<ArticleDto.Response> getPopularArticlesForApp(){
        List<Article> popularArticles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCnt", "viewCnt")).stream().limit(3).collect(Collectors.toList());
        return popularArticles.stream().map(articleMapper::articleResponse).collect(Collectors.toList());
    }
    
    // Article 생성
    public ArticleDto.Response createArticle(AuthorizedMemberDto authorizedMemberDto, ArticleDto.Post postDto) {
        Member member = memberRepository.findById(authorizedMemberDto.getId())
          .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + authorizedMemberDto.getId()));
        Article article = Article.createArticle(postDto, member);
        
        return articleMapper.articleResponse(articleRepository.save(article));
    }
    
    // Article 수정
    public ArticleDto.Response updateArticle(Long id, ArticleDto.Patch patchDto) {
        Article article = articleRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));
        
        article.update(patchDto);
        return articleMapper.articleResponse(article);
    }
    
    // Article 삭제
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));
        
        List<Comment> comments = article.getComments();
        article.setComments(new ArrayList<>());
        articleRepository.delete(article);
        
        // 댓글 삭제
        for (Comment comment : comments) {
            commentRepository.delete(comment);
        }
    }
    
    // 조회수 증가
    public void increaseViewCnt(Long id) {
        Article article = articleRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Article not found ID:" + id));
        article.setViewCnt(article.getViewCnt() + 1);
    }

}
