package com.project.chamong.bookmark.repository;

import com.project.chamong.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM bookmark " +
      "WHERE member_id = :memberId AND content_id = :contentId limit 1", nativeQuery = true)
    Integer existsMemberIdAndContentContentIdAsInteger(long memberId, long contentId);
}
