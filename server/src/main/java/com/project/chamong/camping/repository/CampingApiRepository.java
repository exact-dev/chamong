package com.project.chamong.camping.repository;

import com.project.chamong.camping.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampingApiRepository extends JpaRepository<Content, Long> {
    // 고캠핑 API 특정 키워드 검색
    Page<Content> findByLctClContaining(String keyword, Pageable pageable);

    Page<Content> findByAnimalCmgClNotContaining(String keyword, Pageable pageable);

    Page<Content> findBySbrsClContaining(String keyword, Pageable pageable);

    Page<Content> findByThemaEnvrnClContaining(String keyword, Pageable pageable);

    // 메인페이지
    @Query(value = "SELECT *, COUNT(review.content_id) AS total, SUM(review.rating) AS ratings " +
            "FROM content " +
            "LEFT JOIN review ON content.content_id = review.content_id " +
            "GROUP BY content.content_id " +
            "ORDER BY ratings DESC, total DESC", nativeQuery = true)
    Page<Content> findContents(Pageable pageable);

    // 고캠핑 검색
    @Query(value = "SELECT c FROM Content c WHERE " +
            "    c.facltNm LIKE %:keyword% OR " +
            "    c.lctCl LIKE %:keyword% OR " +
            "    c.themaEnvrnCl LIKE %:keyword% OR " +
            "    c.addr1 LIKE %:keyword% OR " +
            "    c.caravInnerFclty LIKE %:keyword% OR " +
            "    c.glampInnerFclty LIKE %:keyword% OR " +
            "    c.doNm LIKE %:keyword% OR " +
            "    c.exprnProgrm LIKE %:keyword% OR " +
            "    c.intro LIKE %:keyword% OR " +
            "    c.lineIntro LIKE %:keyword% OR " +
            "    c.posblFcltyCl LIKE %:keyword% OR " +
            "    c.sbrsCl LIKE %:keyword% OR " +
            "    c.tel LIKE %:keyword% OR " +
            "    c.exprnProgrmAt LIKE %:keyword% OR " +
            "    c.animalCmgCl LIKE %:keyword% OR " +
            "    c.manageSttus LIKE %:keyword% OR " +
            "    c.doNm LIKE %:place% OR " +
            "    c.doNm LIKE %:placeSecond% OR " +
            "    c.glampInnerFclty LIKE %:thema% OR " +
            "    c.caravInnerFclty LIKE %:thema% OR " +
            "    c.lctCl LIKE %:thema% OR " +
            "    c.sbrsCl LIKE %:thema% OR " +
            "    c.exprnProgrmAt LIKE %:thema% OR " +
            "    c.posblFcltyCl LIKE %:thema% OR " +
            "    c.animalCmgCl LIKE %:thema% OR " +
            "    c.manageSttus LIKE %:thema%")
    Page<Content> findCamping(@Param("keyword") String keyword, @Param("place") String place, @Param("placeSecond") String placeSecond, @Param("thema") String thema, Pageable pageable);

}
