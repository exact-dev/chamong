package com.project.chamong.article.repository;

import com.project.chamong.article.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    List<Article> findByTitleContainingAndIdGreaterThan(String keyword, Long lastArticleId,Pageable pageable);
    @Query("select a from Article a join fetch a.comments where a.id = :id")
    Optional<Article> findWithCommentsById(Long id);

    @Query("select a from Article a where id > :lastArticleId order by a.likeCnt desc, a.viewCnt desc")
    List<Article> findByIdGreaterThan(Long lastArticleId, Pageable pageable);
}
