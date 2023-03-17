package com.project.chamong.community.mapper;

import com.project.chamong.community.dto.CommentDto;
import com.project.chamong.community.entity.Article;
import com.project.chamong.community.entity.Comment;
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
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment toEntity(CommentDto commentDto) {
        if ( commentDto == null ) {
            return null;
        }

        String content = null;

        content = commentDto.getContent();

        Article article = null;

        Comment comment = new Comment( content, article );

        comment.setId( commentDto.getId() );
        comment.setCreateAt( commentDto.getCreateAt() );
        comment.setUpdateAt( commentDto.getUpdateAt() );

        return comment;
    }

    @Override
    public CommentDto toDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDto commentDto = new CommentDto();

        commentDto.setId( comment.getId() );
        commentDto.setContent( comment.getContent() );
        commentDto.setCreateAt( comment.getCreateAt() );
        commentDto.setUpdateAt( comment.getUpdateAt() );

        return commentDto;
    }

    @Override
    public List<CommentDto> toDtoList(List<Comment> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentDto> list = new ArrayList<CommentDto>( comments.size() );
        for ( Comment comment : comments ) {
            list.add( toDto( comment ) );
        }

        return list;
    }
}
