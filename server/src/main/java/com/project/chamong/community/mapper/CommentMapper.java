package com.project.chamong.community.mapper;

import com.project.chamong.community.dto.CommentDto;
import com.project.chamong.community.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);
    Comment toEntity(CommentDto commentDto);

    CommentDto toDto(Comment comment);
    List<CommentDto> toDtoList(List<Comment> comments);
}
