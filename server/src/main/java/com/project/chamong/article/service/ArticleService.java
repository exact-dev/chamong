package com.project.chamong.article.service;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.entity.Article;
import com.project.chamong.article.mapper.ArticleMapper;
import com.project.chamong.article.repository.ArticleRepository;
import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.member.service.MemberService;
import com.project.chamong.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final S3Service s3Service;
    private String dirName = "article_image/";
    
    // 게시글 전체 조회
    @Transactional
    public List<ArticleDto.Response> getArticles(Long lastArticleId, String keyword, Pageable pageable) {
        List<Article> articles = StringUtils.isEmpty(keyword)
          ? articleRepository.findByIdGreaterThan(lastArticleId, pageable)
          : articleRepository.findByTitleContainingAndIdGreaterThan(keyword, lastArticleId,pageable);
        
        List<ArticleDto.Response> articleResponsePage = articles.stream()
          .map(articleMapper::articleResponse).collect(Collectors.toList());
        
        return articleResponsePage;
    }
    
    
    // 상세페이지 - 게시글과 댓글 조회
    @Transactional
    public ArticleDto.Response getArticle(Long id, AuthorizedMemberDto authorizedMemberDto) {
        Member findMember = null;
        
        Article article = articleRepository.findWithCommentsById(id)
          .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_FOUND));
        
        if(authorizedMemberDto != null){
            findMember = memberService.findById(authorizedMemberDto.getId());
        }
        
        increaseViewCnt(article);
        
        //댓글 createdAt 기준으로 내림차순 정렬
        ArticleDto.Response response = articleMapper.articleResponse(article, findMember);
        
        return response;
    }
    
    // 인기글 조회 (5개씩, 1순위: 좋아요 수, 2순위: 조회수)
    public List<ArticleDto.Response> getPopularArticlesForWeb() {
        List<Article> popularArticles = articleRepository.findAll(PageRequest.of(0, 5)).getContent();
        
        return popularArticles.stream().map(articleMapper::articleResponse).collect(Collectors.toList());
    }
    
    // app 화면에서는 3개씩 조회
    public List<ArticleDto.Response> getPopularArticlesForApp() {
        List<Article> popularArticles = articleRepository.findAll(PageRequest.of(0, 3)).getContent();
        
        return popularArticles.stream().map(articleMapper::articleResponse).collect(Collectors.toList());
    }
    
    
    // Article 생성
    public ArticleDto.Response createArticle(AuthorizedMemberDto authorizedMemberDto, ArticleDto.Post postDto, MultipartFile articleImg) {
        Member member = memberRepository.findById(authorizedMemberDto.getId())
          .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + authorizedMemberDto.getId()));
        
        
        if(!articleImg.isEmpty()){
            postDto.setArticleImg(s3Service.uploadFile(articleImg, dirName));
        }
        
        Article article = Article.createArticle(postDto, member);
        
        return articleMapper.articleResponse(articleRepository.save(article));
    }
    
    // Article 수정
    @Transactional
    public ArticleDto.Response updateArticle(AuthorizedMemberDto authorizedMemberDto, Long id, ArticleDto.Patch patchDto, MultipartFile articleImg) {
        Article article = articleRepository.findWithCommentsById(id)
          .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));
        
        if (!article.isWriter(memberService.findById(authorizedMemberDto.getId()))) {
            throw new IllegalStateException("Only the author of the article can delete it.");
        }
        if(articleImg != null){
            patchDto.setArticleImg(s3Service.uploadFile(articleImg, dirName));
        }
        
        article.update(patchDto);
        
        return articleMapper.articleResponse(article);
    }
    
    @Transactional
    // Article 삭제
    public void deleteArticle(AuthorizedMemberDto authorizedMemberDto, Long id) {
        Article article = articleRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));
        
        Member member = memberRepository.findById(authorizedMemberDto.getId())
          .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + authorizedMemberDto.getId()));
        
        if (!article.isWriter(member)) {
            throw new IllegalStateException("Only the author of the article can delete it.");
        }
        
        // 게시글 삭제
        articleRepository.delete(article);
    }
    
    // 조회수 증가
    public void increaseViewCnt(Article article) {
        article.setViewCnt(article.getViewCnt() + 1);
    }

}
