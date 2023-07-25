package com.project.chamong.memberTest;

import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryUnitTest {
  @Autowired
  MemberRepository memberRepository;
  
  @Test
  public void Should_SaveMember_When_MemberDoesNotExist(){
    //given
    Member member = Member.createMember(
      MemberDto.Post
        .builder()
        .email("test@gmail.com")
        .nickname("jPark")
        .password("1q2w3e4r!")
        .build()
    );
    
    Member savedMember = memberRepository.save(member);
    
    Assertions.assertThat("test@gmail.com").isEqualTo(savedMember.getEmail());
    Assertions.assertThat("jPark").isEqualTo(savedMember.getNickname());
    Assertions.assertThat("1q2w3e4r!").isEqualTo(savedMember.getPassword());
  }
  
  @Test
  public void Should_FindMember_When_MemberExist(){
    Member member = memberRepository.findByEmail("a115@naver.com").orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    
    Assertions.assertThat("a115@naver.com").isEqualTo(member.getEmail());
    Assertions.assertThat("jPark").isEqualTo(member.getNickname());
  }
  
  @Test
  public void Should_ThrowBusinessLogicException_When_MemberDoesNotExist(){
    Optional<Member> optionalMember = memberRepository.findByEmail("Invalid-Email");
    
    org.junit.jupiter.api.Assertions.assertThrows(
      BusinessLogicException.class, () -> optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND))
    );
  }
  
  @Test
  public void Should_DeleteMember_When_MemberExist() {
    Member member = Member.createMember(
      MemberDto.Post
        .builder()
        .email("test@example.com")
        .build()
    );
    Member savedMember = memberRepository.save(member);
    
    Assertions.assertThat("test@example.com").isEqualTo(savedMember.getEmail());
    
    memberRepository.deleteByEmail("test@example.com");
    
    org.junit.jupiter.api.Assertions.assertThrows(
      BusinessLogicException.class, () -> memberRepository.findByEmail("test@example.com")
        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND))
    );
  }
  
}
