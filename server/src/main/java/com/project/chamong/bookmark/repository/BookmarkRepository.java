package com.project.chamong.bookmark.repository;

import com.project.chamong.bookmark.dto.BookmarkDto;
import com.project.chamong.bookmark.entity.Bookmark;
import com.project.chamong.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM bookmark WHERE member_id = :memberId AND content_id = :contentId", nativeQuery = true)
    Integer existsMemberIdAndContentContentIdAsInteger(@Param("memberId") long memberId, @Param("contentId") long contentId);

}
