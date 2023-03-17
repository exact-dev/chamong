package com.project.chamong.community.mapper;

import com.project.chamong.community.dto.ArticleDto;
import com.project.chamong.community.entity.Article;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-17T00:24:59+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.18 (Azul Systems, Inc.)"
)
@Component
public class ArticleMapperImpl implements ArticleMapper {

    @Override
    public Article toEntity(ArticleDto dto) {
        if ( dto == null ) {
            return null;
        }

        Article article = new Article();

        article.setTitle( dto.getTitle() );
        article.setContent( dto.getContent() );
        article.setArticleImg( dto.getArticleImg() );
        article.setUpdateAt( dto.getUpdateAt() );

        return article;
    }

    @Override
    public ArticleDto toDto(Article entity) {
        if ( entity == null ) {
            return null;
        }

        ArticleDto articleDto = new ArticleDto();

        articleDto.setTitle( entity.getTitle() );
        articleDto.setContent( entity.getContent() );
        articleDto.setArticleImg( entity.getArticleImg() );
        articleDto.setUpdateAt( entity.getUpdateAt() );

        return articleDto;
    }

    @Override
    public List<Article> toDtoList(List<Article> articles) {
        if ( articles == null ) {
            return null;
        }

        List<Article> list = new ArrayList<Article>( articles.size() );
        for ( Article article : articles ) {
            list.add( article );
        }

        return list;
    }
}
