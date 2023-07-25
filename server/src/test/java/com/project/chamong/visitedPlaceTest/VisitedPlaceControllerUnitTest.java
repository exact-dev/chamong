package com.project.chamong.visitedPlaceTest;

import com.project.chamong.auth.config.SecurityConfiguration;
import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.auth.jwt.JwtProvider;
import com.project.chamong.auth.repository.TokenRedisRepository;
import com.project.chamong.auth.utils.CustomAuthorityUtils;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.place.controller.VisitedPlaceController;
import com.project.chamong.place.dto.VisitedPlaceDto;
import com.project.chamong.place.service.VisitedPlaceService;
import com.project.chamong.s3.service.S3Service;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@WebMvcTest(value = VisitedPlaceController.class, includeFilters = {
  @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class),
  @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtProvider.class),
  @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CustomAuthorityUtils.class)
})
@MockBean(JpaMetamodelMappingContext.class)
public class VisitedPlaceControllerUnitTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private CustomAuthorityUtils customAuthorityUtils;
  
  @Autowired
  private JwtProvider jwtProvider;
  
  @MockBean
  private VisitedPlaceService visitedPlaceService;
  
  @MockBean
  private MemberRepository memberRepository;
  
  @MockBean
  private TokenRedisRepository redisRepository;
  
  @MockBean
  private S3Service s3Service;
  
  @Test
  public void Should_SaveVisitedPlace_When_AuthenticationSuccess_And_PathVariable_Is_Positive() throws Exception {
    //given
    Member member = Member.createMember(
      MemberDto.Post.builder()
        .email("test@example.com")
        .nickname("jPark")
        .password("1q2w3e4r!")
        .roles(customAuthorityUtils.crateRoles("test@example.com"))
        .build());
    
    VisitedPlaceDto.Response response = VisitedPlaceDto.Response
      .builder()
      .id(1L)
      .facltNm("(주)디노담양힐링파크 지점")
      .lineIntro("담양 힐링파크에서 일상 속 쌓인 스트레스를 풀어보자")
      .addr1("전남 담양군 봉산면 탄금길 9-26")
      .firstImageUrl("https://gocamping.or.kr/upload/camp/4/thumb/thumb_720_4548WQ5JCsRSkbHrBAaZylrQ.jpg")
      .build();
    
    BDDMockito.given(visitedPlaceService.saveVisitedPlace(Mockito.anyLong(), Mockito.any(AuthorizedMemberDto.class)))
      .willReturn(response);
    
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(member));
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .post("/visited-places/1")
        .header("Authorization", "Bearer " + jwtProvider.generateAccessToken(member))
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.facltNm").value("(주)디노담양힐링파크 지점"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.lineIntro").value("담양 힐링파크에서 일상 속 쌓인 스트레스를 풀어보자"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.addr1").value("전남 담양군 봉산면 탄금길 9-26"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.firstImageUrl")
        .value("https://gocamping.or.kr/upload/camp/4/thumb/thumb_720_4548WQ5JCsRSkbHrBAaZylrQ.jpg"));
    
  }
  
  @Test
  public void Should_VisitedPlace_Delete_When_AuthenticationSuccess_And_PathVariable_Is_Positive() throws Exception {
    //given
    Member member = Member.createMember(
      MemberDto.Post.builder()
        .email("test@example.com")
        .nickname("jPark")
        .password("1q2w3e4r!")
        .roles(customAuthorityUtils.crateRoles("test@example.com"))
        .build());
    
    BDDMockito.doNothing().when(visitedPlaceService).deleteVisitedPlace(Mockito.anyLong(), Mockito.any(AuthorizedMemberDto.class));
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(member));
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .delete("/visited-places/1")
        .header("Authorization", "Bearer " + jwtProvider.generateAccessToken(member))
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(MockMvcResultMatchers.content().string("방문한 장소가 삭제 되었습니다."));
  }
}
