package com.project.chamong.article.mapper;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.article.entity.Article;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    Article articlePostDtoToArticle(ArticleDto.Post postDto);
    Article articlePatchDtoToArticle(ArticleDto.Patch patchDto);
    ArticleDto.Response articleResponse(Article article);
    List<ArticleDto.Response> articleResponseList(List<Article> articles);
}
