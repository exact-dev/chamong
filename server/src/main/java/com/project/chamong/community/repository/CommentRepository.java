package com.project.chamong.community.repository;

import com.project.chamong.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByCommunityIdOrderByCreateDateAsc(Long communityId);
}
