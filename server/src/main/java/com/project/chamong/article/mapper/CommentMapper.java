package com.project.chamong.article.mapper;

import com.project.chamong.article.dto.CommentDto;
import com.project.chamong.article.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment commentPostDtoToComment(CommentDto.Post postDto);
    Comment commentPatchDtoToComment(CommentDto.Patch patchDto);

    List<CommentDto> toDtoList(List<Comment> comments);
    CommentDto.Response commentResponse(Comment comment);
}
