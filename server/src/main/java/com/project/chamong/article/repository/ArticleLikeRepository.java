package com.project.chamong.article.repository;

import com.project.chamong.article.entity.ArticleLike;
import com.project.chamong.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Optional<ArticleLike> findByMemberIdAndArticleId(Long memberId, Long articleId);
    long countByArticleId(Long articleId);
}
