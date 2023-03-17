package com.project.chamong.community.mapper;

import com.project.chamong.community.dto.ArticleDto;
import com.project.chamong.community.dto.ArticleLikeDto;
import com.project.chamong.community.entity.Article;
import com.project.chamong.community.entity.ArticleLike;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ArticleLikeMapper {
    ArticleLikeMapper INSTANCE = Mappers.getMapper(ArticleLikeMapper.class);
    ArticleLike toEntity(ArticleLikeDto dto);
    ArticleLikeDto toDto(ArticleLike entity);
    List<ArticleLikeDto> toDtoList(List<ArticleLike> articleLikes);
}
