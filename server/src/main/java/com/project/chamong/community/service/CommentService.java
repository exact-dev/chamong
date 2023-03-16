package com.project.chamong.community.service;

import com.project.chamong.community.dto.CommentDto;
import com.project.chamong.community.entity.Comment;
import com.project.chamong.community.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository repository;

    // 댓글 작성자, content, 글 작성 시간
//    public Long createComment(Comment request){
//        Comment comment = new Comment(request.getContent(), request.getId(),request.getCreateAt());
//    }

    @Transactional(readOnly = true)
    public List<Comment> getCommetsByCommunityId(Long communityId){
        List<Comment> comments = repository.findByCommunityIdOrderByCreateDateAsc(communityId);
        return comments.stream()
                .map(comment -> Comment.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .createAt(comment.getCreateAt())
                        .updateAt(comment.getUpdateAt())
                        .build())
                .collect(Collectors.toList());
    }

    public void updateComment(Long id, String content){
        Comment comment = repository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다. id = "+id));
        comment.update(content);
    }
    public void deleteComment(Long id){
        repository.deleteById(id);
    }
}
