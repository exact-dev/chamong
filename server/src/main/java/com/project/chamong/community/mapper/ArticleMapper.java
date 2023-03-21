package com.project.chamong.community.mapper;

import com.project.chamong.community.dto.ArticleDto;
import com.project.chamong.community.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);
    //List<ArticleDto.Response> articleResponse(List<Article> articleList);
    Article toEntity(ArticleDto dto);
    ArticleDto toDto(Article entity);
    List<Article> toDtoList(List<Article> articles);
}
