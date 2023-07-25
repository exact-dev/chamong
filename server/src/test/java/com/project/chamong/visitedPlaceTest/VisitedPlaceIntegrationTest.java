package com.project.chamong.visitedPlaceTest;

import com.project.chamong.auth.jwt.JwtProvider;
import com.project.chamong.auth.utils.CustomAuthorityUtils;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.place.dto.VisitedPlaceDto;
import com.project.chamong.place.entity.VisitedPlace;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.annotation.Nullable;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VisitedPlaceIntegrationTest {
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private JwtProvider jwtProvider;
  
  @Autowired
  private MemberRepository memberRepository;
  
  @Autowired
  private CustomAuthorityUtils customAuthorityUtils;
  
  @Test
  public void Should_Save_VisitedPlace_When_AuthenticationSuccess() throws Exception {
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .post("/visited-places/1")
        .header("Authorization", "Bearer " + generateAccessToken(null))
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.contentId").value(1))
      .andExpect(MockMvcResultMatchers.jsonPath("$.facltNm").value("(주)디노담양힐링파크 지점"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.lineIntro").value("담양 힐링파크에서 일상 속 쌓인 스트레스를 풀어보자"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.addr1").value("전남 담양군 봉산면 탄금길 9-26"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.firstImageUrl").value("https://gocamping.or.kr/upload/camp/4/thumb/thumb_720_4548WQ5JCsRSkbHrBAaZylrQ.jpg"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.mapX").value(126.9609528))
      .andExpect(MockMvcResultMatchers.jsonPath("$.mapY").value(35.2714369))
      .andDo(MockMvcResultHandlers.print());
  }
  
  @Test
  public void Should_Delete_VisitedPlace_When_AuthenticationSuccess_And_Have_Permission() throws Exception {
    Member member = Member.createMember(
      MemberDto.Post.builder()
        .email("test@example.com")
        .nickname("jPark")
        .password("1q2w3e4r!")
        .roles(customAuthorityUtils.crateRoles("test@example.com"))
        .build()
    );
    
    VisitedPlace visitedPlace = VisitedPlace.createVisitedPlace(
      VisitedPlaceDto.Post
        .builder()
        .content(new Content())
        .build()
    );
    
    visitedPlace.setMember(member);
    
    String accessToken = generateAccessToken(member);
    
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .delete("/visited-places/" + visitedPlace.getId())
        .header("Authorization", "Bearer " + accessToken)
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(MockMvcResultMatchers.content().string("방문한 장소가 삭제 되었습니다."));
  }
  
  @Test
  public void Should_Handle_BusinessLogicException_When_Have_NoPermission() throws Exception {
    //given
    Member member = Member.createMember(
      MemberDto.Post.builder()
        .email("test2@example.com")
        .build()
    );
    
    VisitedPlace visitedPlace = VisitedPlace.createVisitedPlace(
      VisitedPlaceDto.Post
        .builder()
        .content(new Content())
        .build()
    );
    
    visitedPlace.setMember(member);
    
    memberRepository.save(member);
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .delete("/visited-places/" + visitedPlace.getId())
        .header("Authorization", "Bearer " + generateAccessToken(null))
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("방문한 장소를 삭제할 권한이 없습니다."));
  }
  
  public String generateAccessToken(@Nullable Member member){
    if(member == null){
      member = Member.createMember(
        MemberDto.Post.builder()
          .email("test@example.com")
          .nickname("jPark")
          .password("1q2w3e4r!")
          .roles(customAuthorityUtils.crateRoles("test@example.com"))
          .build()
      );
    }
    
    memberRepository.save(member);
    
    return jwtProvider.generateAccessToken(member);
  }
}
