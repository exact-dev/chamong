package com.project.chamong.article.mapper;

import com.project.chamong.article.dto.CommentDto;
import com.project.chamong.article.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    Comment commentPostDtoToComment(CommentDto.Post postDto);
    Comment commentPatchDtoToComment(CommentDto.Patch patchDto);
    default CommentDto.Response commentResponse(Comment comment){
        return CommentDto.Response.builder()
          .id(comment.getId())
          .content(comment.getContent())
          .articleId(comment.getArticle().getId())
          .memberId(comment.getMember().getId())
          .nickname(comment.getMember().getNickname())
          .profileImg(comment.getMember().getProfileImg())
          .createdAt(comment.getCreatedAt())
          .updatedAt(comment.getUpdatedAt())
          .build();
    }
}
