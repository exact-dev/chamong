package com.project.chamong.article.service;

import com.project.chamong.article.dto.CommentDto;
import com.project.chamong.article.entity.Article;
import com.project.chamong.article.entity.Comment;
import com.project.chamong.article.mapper.CommentMapper;
import com.project.chamong.article.repository.ArticleRepository;
import com.project.chamong.article.repository.CommentRepository;
import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final CommentMapper commentMapper;
    
    // 댓글 생성
    @Transactional
    public CommentDto.Response createComment(AuthorizedMemberDto authorizedMemberDto, Long articleId, CommentDto.Post postDto) {
        Comment comment = commentMapper.commentPostDtoToComment(postDto);
        Member member = memberRepository.findById(authorizedMemberDto.getId())
          .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + authorizedMemberDto.getId()));
        Article article = articleRepository.findById(articleId)
          .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));
        
        comment.setArticle(article);
        comment.setMember(member);
        Comment savedComment = commentRepository.save(comment);
        
        return commentMapper.commentResponse(savedComment);
    }
    
    // 댓글 수정
    @Transactional
    public CommentDto.Response updateComment(AuthorizedMemberDto authorizedMemberDto, Long articleId, Long id, CommentDto.Patch patchDto) {
        Comment comment = commentRepository.findByIdAndArticleId(id, articleId)
          .orElseThrow(() -> new IllegalArgumentException("Comment not found with ID: " + id));
        if (!comment.getMember().getId().equals(authorizedMemberDto.getId())) {
            throw new IllegalArgumentException("Only the author of the comment can delete it.");
        }
        Comment updatedComment = commentMapper.commentPatchDtoToComment(patchDto);
        comment.setContent(updatedComment.getContent());
        
        return commentMapper.commentResponse(comment);
    }
    
    // 댓글 삭제
    @Transactional
    public void deleteComment(AuthorizedMemberDto authorizedMemberDto, Long articleId, Long id) {
        Comment comment = commentRepository.findByIdAndArticleId(id, articleId)
          .orElseThrow(() -> new IllegalArgumentException("Comment not found with ID: " + id + "for Article ID: " + articleId));
        if (!comment.getMember().getId().equals(authorizedMemberDto.getId())) {
            throw new IllegalArgumentException("Only the author of the comment can delete it.");
        }
        commentRepository.deleteById(id);
    }
}
