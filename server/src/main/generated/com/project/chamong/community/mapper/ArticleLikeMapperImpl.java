package com.project.chamong.community.mapper;

import com.project.chamong.community.dto.ArticleLikeDto;
import com.project.chamong.community.entity.ArticleLike;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-17T00:24:59+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.18 (Azul Systems, Inc.)"
)
public class ArticleLikeMapperImpl implements ArticleLikeMapper {

    @Override
    public ArticleLike toEntity(ArticleLikeDto dto) {
        if ( dto == null ) {
            return null;
        }

        ArticleLike.ArticleLikeBuilder articleLike = ArticleLike.builder();

        articleLike.id( dto.getId() );

        return articleLike.build();
    }

    @Override
    public ArticleLikeDto toDto(ArticleLike entity) {
        if ( entity == null ) {
            return null;
        }

        ArticleLikeDto articleLikeDto = new ArticleLikeDto();

        articleLikeDto.setId( entity.getId() );

        return articleLikeDto;
    }

    @Override
    public List<ArticleLikeDto> toDtoList(List<ArticleLike> articleLikes) {
        if ( articleLikes == null ) {
            return null;
        }

        List<ArticleLikeDto> list = new ArrayList<ArticleLikeDto>( articleLikes.size() );
        for ( ArticleLike articleLike : articleLikes ) {
            list.add( toDto( articleLike ) );
        }

        return list;
    }
}
