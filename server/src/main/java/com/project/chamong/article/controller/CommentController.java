package com.project.chamong.article.controller;

import com.project.chamong.article.dto.CommentDto;
import com.project.chamong.article.service.CommentService;
import com.project.chamong.auth.dto.AuthorizedMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    
    // 댓글 생성
    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<CommentDto.Response> createComment(@AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto, @PathVariable Long articleId, @RequestBody CommentDto.Post postDto){
        CommentDto.Response response = commentService.createComment(authorizedMemberDto,articleId, postDto);
        return ResponseEntity.ok(response);
    }
    
    // 댓글 수정
    @PatchMapping("/articles/{articleId}/comments/{id}")
    public ResponseEntity<CommentDto.Response> updateComment(@AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto,@PathVariable Long articleId, @PathVariable Long id, @RequestBody CommentDto.Patch patchDto){
        
        return ResponseEntity.ok(commentService.updateComment(authorizedMemberDto,articleId, id, patchDto));
    }
    
    // 댓글 삭제
    @DeleteMapping("/articles/{articleId}/comments/{id}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto, @PathVariable Long articleId, @PathVariable Long id){
        commentService.deleteComment(authorizedMemberDto,articleId,id);
        return ResponseEntity.noContent().build();
    }
}
