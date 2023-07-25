package com.project.chamong.article.repository;

import com.project.chamong.article.entity.Comment;
import com.project.chamong.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select c from Comment c where c.id = :id And c.article.id= :articleId")
    Optional<Comment> findByIdAndArticleId(Long id, Long articleId);
    List<Comment> findByMember(Member member);
}
