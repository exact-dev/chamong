package com.project.chamong.review.repository;

import com.project.chamong.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  
  @Query(value = "SELECT r FROM Review r JOIN FETCH r.member WHERE " +
    "r.contents.contentId = :contentId")
  List<Review> findWithMemberByContentId(Long contentId);
}
