package com.project.chamong.article.mapper;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.entity.Article;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    ArticleDto.Response articleResponse(Article article);
}
