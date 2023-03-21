package com.project.chamong.bookmark.mapper;

import com.project.chamong.bookmark.dto.BookmarkDto;
import com.project.chamong.bookmark.entity.Bookmark;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-21T16:14:15+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.18 (Azul Systems, Inc.)"
)
@Component
public class BookmarkMapperImpl implements BookmarkMapper {

    @Override
    public Bookmark bookmarkPostDtoToBookmark(BookmarkDto.Post requestBody) {
        if ( requestBody == null ) {
            return null;
        }

        Bookmark bookmark = new Bookmark();

        return bookmark;
    }

    @Override
    public BookmarkDto.Response bookmarkResponse(Bookmark bookmark) {
        if ( bookmark == null ) {
            return null;
        }

        BookmarkDto.Response response = new BookmarkDto.Response();

        return response;
    }
}
