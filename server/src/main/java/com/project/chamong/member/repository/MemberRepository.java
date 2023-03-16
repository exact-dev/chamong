package com.project.chamong.member.repository;

import com.project.chamong.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
  
  public Optional<Member> findByEmail(String email);
  
  public void deleteByEmail(String email);
}
