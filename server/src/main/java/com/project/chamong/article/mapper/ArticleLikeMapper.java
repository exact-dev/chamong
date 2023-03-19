package com.project.chamong.article.mapper;

import com.project.chamong.article.dto.ArticleLikeDto;
import com.project.chamong.article.entity.ArticleLike;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleLikeMapper {
    ArticleLike articleLikePostToarticleLike(ArticleLikeDto.Post postDto);
    List<ArticleLikeDto> toDtoList(List<ArticleLike> articleLikes);
}
