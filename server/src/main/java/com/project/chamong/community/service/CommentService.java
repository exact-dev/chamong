package com.project.chamong.community.service;

import com.project.chamong.community.dto.CommentDto;
import com.project.chamong.community.entity.Comment;
import com.project.chamong.community.exception.CommentNotFoundException;
import com.project.chamong.community.mapper.CommentMapper;
import com.project.chamong.community.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public Long saveComment(CommentDto commentDto){
        Comment comment = commentMapper.toEntity(commentDto);
        return commentRepository.save(comment).getId();
    }


    public CommentDto updateComment(Long id, CommentDto commentDto){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글이 없습니다. id = "+id));
        comment.setContent(commentDto.getContent());
        comment.setUpdateAt(LocalDateTime.now());
        // commentRepository.save(comment);
        return CommentMapper.INSTANCE.toDto(commentRepository.save(comment));
    }

    public void deleteComment(Long id){
        Comment comment = commentRepository.findById(id)
                        .orElseThrow(()-> new CommentNotFoundException(id));
        commentRepository.delete(comment);
        //commentRepository.deleteById(id);
    }

    public CommentDto findById(Long id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(()-> new CommentNotFoundException(id));
        return commentMapper.toDto(comment);
    }

    public List<CommentDto> findAll(){
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }
}
