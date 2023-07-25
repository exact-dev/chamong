package com.project.chamong.place.repository;

import com.project.chamong.place.entity.VisitedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitedPlaceRepository extends JpaRepository<VisitedPlace, Long> {
  
  @Query("select v from VisitedPlace v join fetch v.content")
  List<VisitedPlace> findWithContentAll();
  @Query("select v.id from VisitedPlace v where v.member.id = :memberId and v.content.contentId = :contentId")
  VisitedPlace findByMemberIdAndContentId(Long memberId, Long contentId);
}
