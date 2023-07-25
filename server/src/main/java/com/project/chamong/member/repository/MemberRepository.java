package com.project.chamong.member.repository;

import com.project.chamong.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByEmail(String email);
  
  void deleteByEmail(String email);
  
  @Query("select m from Member m join fetch m.articles where m.email = :email")
  Member findWithArticleByEmail(String email);
  
  @Query("select m from Member m join fetch m.myPlaces where m.id = :id")
  Optional<Member> findWithCommentsById(Long id);
}
