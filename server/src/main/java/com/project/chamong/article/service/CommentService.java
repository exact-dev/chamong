package com.project.chamong.article.service;

import com.project.chamong.article.dto.CommentDto;
import com.project.chamong.article.entity.Article;
import com.project.chamong.article.entity.Comment;
import com.project.chamong.article.mapper.CommentMapper;
import com.project.chamong.article.repository.ArticleRepository;
import com.project.chamong.article.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final Article article;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentDto.Response createComment(CommentDto.Post postDto) {
        Comment comment = commentMapper.commentPostDtoToComment(postDto);
        commentRepository.save(comment);
        article.increaseCommentCnt();
        return commentMapper.commentResponse(comment);
    }

    @Transactional
    public CommentDto.Response updateComment(Long id, CommentDto.Patch patchDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with ID: " + id));
        Comment updatedComment = commentMapper.commentPatchDtoToComment(patchDto);
        comment.setContent(updatedComment.getContent());

        return commentMapper.commentResponse(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
        article.decreaseCommentCnt();
    }

    @Transactional
    // 게시글에 대한 댓글 목록 조회
    public List<CommentDto.Response> getCommentsByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        return comments.stream()
                .map(commentMapper::commentResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    // 게시글에 대한 댓글 수 조회
    public long getCommentCntByArticleId(Long articleId) {
        return commentRepository.countByArticleId(articleId);
    }
}
