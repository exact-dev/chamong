package com.project.chamong.community.repository;

import com.project.chamong.community.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Object deleteByCommunityAndUserId(Long communityId, Long userId);
}
