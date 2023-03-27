package com.project.chamong.article.controller;

import com.project.chamong.article.dto.CommentDto;
import com.project.chamong.article.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    
    // 댓글 생성
    @PostMapping("/comments")
    public ResponseEntity<CommentDto.Response> createComment(@RequestBody CommentDto.Post postDto){
        return ResponseEntity.ok(commentService.createComment(postDto));
    }
    
    // 댓글 수정
    @PatchMapping("/comments/{id}")
    public ResponseEntity<CommentDto.Response> updateComment(@PathVariable Long id, @RequestBody CommentDto.Patch patchDto){
        return ResponseEntity.ok(commentService.updateComment(id, patchDto));
    }
    
    // 댁글 삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
