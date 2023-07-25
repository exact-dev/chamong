package com.project.chamong.bookmark.service;

import com.project.chamong.bookmark.entity.Bookmark;
import com.project.chamong.bookmark.repository.BookmarkRepository;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    // 북마크 추가
    public Bookmark createBookmark(Bookmark bookmark) {
        return bookmarkRepository.save(bookmark);
    }

    // 북마크 삭제
    @Transactional
    public void deleteBookmark(long bookmarkId) {
        Bookmark bookmark = findVerifiedBookmark(bookmarkId);
        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public Bookmark findVerifiedBookmark(long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId)
          .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOOKMARK_NOT_FOUND));
    }
}
