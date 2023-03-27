package com.project.chamong.article.repository;

import com.project.chamong.article.entity.Comment;
import com.project.chamong.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByArticleId(Long articleId);
    List<Comment> findByMember(Member member);
}
