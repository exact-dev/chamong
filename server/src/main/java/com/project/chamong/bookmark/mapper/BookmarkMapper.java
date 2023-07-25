package com.project.chamong.bookmark.mapper;

import com.project.chamong.bookmark.dto.BookmarkDto;
import com.project.chamong.bookmark.entity.Bookmark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookmarkMapper {
    BookmarkDto.Response bookmarkResponse(Bookmark bookmark);
}
