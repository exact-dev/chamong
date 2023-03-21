package com.project.chamong.bookmark.controller;


import com.project.chamong.bookmark.dto.BookmarkDto;
import com.project.chamong.bookmark.entity.Bookmark;
import com.project.chamong.bookmark.mapper.BookmarkMapper;
import com.project.chamong.bookmark.service.BookmarkService;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.camping.service.CampingApiService;
import com.project.chamong.member.entity.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    private final CampingApiService campingApiService;

    private final BookmarkMapper mapper;

    public BookmarkController(BookmarkService bookmarkService,
                              CampingApiService campingApiService,
                              BookmarkMapper mapper){
        this.bookmarkService = bookmarkService;
        this.campingApiService = campingApiService;
        this.mapper = mapper;
    }

    @PostMapping("/{content-id}")
    public ResponseEntity postBookmark(@PathVariable("content-id") long contentId,
                                       @AuthenticationPrincipal Member member,
                                       @RequestBody BookmarkDto.Post requestBody){
        Content content = campingApiService.findContent(contentId);
        Bookmark bookmark = mapper.bookmarkPostDtoToBookmark(requestBody);
        bookmark.setMember(member);
        bookmark.setContent(content);
        Bookmark createdBookmark = bookmarkService.createBookmark(bookmark);

        return new ResponseEntity<>(mapper.bookmarkResponse(createdBookmark), HttpStatus.CREATED);
    }
}
