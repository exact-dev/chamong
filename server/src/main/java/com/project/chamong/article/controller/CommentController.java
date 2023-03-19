package com.project.chamong.article.controller;

import com.project.chamong.article.dto.CommentDto;
import com.project.chamong.article.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 목록 조회
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto.Response> getComment(@PathVariable Long id){
        CommentDto.Response comment = commentService.getComment(id);
        return ResponseEntity.ok(comment);
    }

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentDto.Response> createComment(@RequestBody CommentDto.Post postDto){
        CommentDto.Response response = commentService.createComment(postDto);
        return ResponseEntity.ok(response);
    }

    // 댓글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<CommentDto.Response> updateComment(@PathVariable Long id, @RequestBody CommentDto.Patch patchDto){
        CommentDto.Response response = commentService.updateComment(id, patchDto);
        return ResponseEntity.ok(response);
    }
    // 댁글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }


}
