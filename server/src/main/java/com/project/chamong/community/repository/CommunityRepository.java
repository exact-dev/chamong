package com.project.chamong.community.repository;

import com.project.chamong.community.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByCommunityId(Long communityId);

}
