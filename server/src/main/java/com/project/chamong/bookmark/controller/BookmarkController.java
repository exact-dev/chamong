package com.project.chamong.bookmark.controller;


import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.bookmark.entity.Bookmark;
import com.project.chamong.bookmark.mapper.BookmarkMapper;
import com.project.chamong.bookmark.service.BookmarkService;
import com.project.chamong.camping.dto.ContentResponseDto;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.camping.service.CampingApiService;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    private final CampingApiService campingApiService;

    private final MemberService memberService;

    private final BookmarkMapper mapper;

    public BookmarkController(BookmarkService bookmarkService,
                              CampingApiService campingApiService,
                              MemberService memberService,
                              BookmarkMapper mapper){
        this.bookmarkService = bookmarkService;
        this.campingApiService = campingApiService;
        this.memberService = memberService;
        this.mapper = mapper;
    }

    // 북마크 추가
    @PostMapping("/{content-id}")
    public ResponseEntity postBookmark(@PathVariable("content-id") long contentId,
                                       @AuthenticationPrincipal AuthorizedMemberDto authorizedMember){
        Content content = campingApiService.findContent(contentId);
        Bookmark bookmark = new Bookmark();
        Member findMember = memberService.findByEmail(authorizedMember.getEmail());
        bookmark.setMember(findMember);
        bookmark.setContent(content);
        Bookmark createdBookmark = bookmarkService.createBookmark(bookmark);

        return new ResponseEntity<>(mapper.bookmarkResponse(createdBookmark), HttpStatus.CREATED);
    }

    // 북마크 취소
    @DeleteMapping("/{bookmark-id}")
    public ResponseEntity<?> deleteBookmark(@PathVariable("bookmark-id") long bookmarkId){
        bookmarkService.deleteBookmark(bookmarkId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 위시리스트 조회
    @GetMapping
    public ResponseEntity<Page<ContentResponseDto>> getBookmark(@RequestParam int page,
                                                                @AuthenticationPrincipal @Nullable AuthorizedMemberDto authorizedMemberDto){
        Page<ContentResponseDto> contents = campingApiService.findBookmark(page, authorizedMemberDto);
        return new ResponseEntity<>(contents, HttpStatus.OK);
    }
}
