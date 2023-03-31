package com.project.chamong.article.service;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.entity.Article;
import com.project.chamong.article.entity.Comment;
import com.project.chamong.article.mapper.ArticleMapper;
import com.project.chamong.article.repository.ArticleLikeRepository;
import com.project.chamong.article.repository.ArticleRepository;
import com.project.chamong.article.repository.CommentRepository;
import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.member.service.MemberService;
import com.project.chamong.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private final S3Service s3Service;
    private String dirName = "article_image/";
    
    // 게시글 전체 조회
    // 게시글 전체 조회
    @Transactional
    public Page<ArticleDto.Response> getArticles(String keyword, Pageable pageable) {
        Page<Article> articlePage = StringUtils.isEmpty(keyword)
          ? articleRepository.findAll(pageable)
          : articleRepository.findByTitleContaining(keyword, pageable);
    
        Page<ArticleDto.Response> articleResponsePage = articlePage.map(articleMapper::articleResponse);
    
        for (ArticleDto.Response articleResponse : articleResponsePage.getContent()) {
            Article article = articleRepository.findById(articleResponse.getId())
              .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_FOUND));
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
        Member findMember = null;
        Article article = articleRepository.findById(id)
          .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_FOUND));
        if(authorizedMemberDto != null){
            findMember = memberService.findByEmail(authorizedMemberDto.getEmail());
        }
        
        increaseViewCnt(id);
        
        //댓글 createdAt 기준으로 내림차순 정렬
        List<Comment> comments = article.getComments();
        Collections.sort(comments, Comparator.comparing(Comment::getCreatedAt));
        ArticleDto.Response response = articleMapper.articleResponse(article, findMember);
        response.setComments(articleMapper.commentsToCommentResponseDto(comments));
        
        return response;
    }
    
    // 인기글 조회 (5개씩, 1순위: 좋아요 수, 2순위: 조회수)
    public List<ArticleDto.Response> getPopularArticlesForWeb() {
        List<Article> popularArticles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCnt", "viewCnt"))
          .stream().limit(5).collect(Collectors.toList());
        return popularArticles.stream().map(articleMapper::articleResponse).collect(Collectors.toList());
    }
    
    // app 화면에서는 3개씩 조회
    public List<ArticleDto.Response> getPopularArticlesForApp() {
        List<Article> popularArticles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCnt", "viewCnt"))
          .stream().limit(3).collect(Collectors.toList());
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
    public ArticleDto.Response updateArticle(AuthorizedMemberDto authorizedMemberDto, Long id, ArticleDto.Patch patchDto, MultipartFile articleImg) {
        Article article = articleRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));
        
        if (!article.isWriter(memberService.findByEmail(authorizedMemberDto.getEmail()))) {
            throw new IllegalStateException("Only the author of the article can delete it.");
            
        }
        if(!articleImg.isEmpty()){
            patchDto.setArticleImg(s3Service.uploadFile(articleImg, dirName));
        }
        
        
        article.update(patchDto);
        articleRepository.save(article);
        
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
        
        if (article.getMember().getId().equals(member)) {
            throw new IllegalStateException("Only the author of the article can delete it.");
        }
        List<Comment> comments = article.getComments();
        article.setComments(new ArrayList<>());
        // article 삭제 전에 연관된 comments 컬렉션 비우기
        comments.forEach(comment -> comment.setArticle(null));
        commentRepository.deleteAll(comments);
        
        // 게시글 삭제
        articleRepository.delete(article);
    }
    
    // 조회수 증가
    public void increaseViewCnt(Long id) {
        Article article = articleRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Article not found ID:" + id));
        article.setViewCnt(article.getViewCnt() + 1);
    }

}
