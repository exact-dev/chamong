package com.project.chamong.memberTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.project.chamong.article.service.ArticleService;
import com.project.chamong.auth.jwt.JwtProvider;
import com.project.chamong.auth.repository.TokenRedisRepository;
import com.project.chamong.auth.utils.CustomAuthorityUtils;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ErrorResponse;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberIntegrationTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private Gson gson;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  JwtProvider jwtProvider;
  
  @Autowired
  private MemberRepository memberRepository;
  
  @Autowired
  private MemberService memberService;
  
  @Autowired
  private CustomAuthorityUtils customAuthorityUtils;
  
  @Autowired
  private ArticleService articleService;
  
  @Autowired
  private TokenRedisRepository redisRepository;
  
  @Test
  @DisplayName("Post 요청 후 데이터베이스에 이미 같은 Email을 가진 Member가 존재할 경우 BusinessLogicException 발생하는지 테스트 합니다.")
  public void Should_HandleBusinessLogicException_When_MemberEmailExist() throws Exception {
    //given
    MemberDto.Post postDto = MemberDto.Post.builder()
      .email("test@example.com")
      .nickname("jPark")
      .password("1q2w3e4r!")
      .build();
    
    String requestBodyData = objectMapper.writeValueAsString(postDto);
    
    String errorResponse = objectMapper.writeValueAsString(ErrorResponse.of(ExceptionCode.MEMBER_EXISTS));
    
    memberRepository.save(Member.createMember(postDto));
    
    
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .post("/members")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBodyData)
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isConflict())
      .andExpect(MockMvcResultMatchers.content().string(errorResponse))
      .andDo(MockMvcResultHandlers.print());
  }
  
  @Test
  @DisplayName("Post 요청시 PostDto의 각 필드가 검증에 통과할 경우 데이터베이스에 Member를 저장합니다.")
  public void should_SaveMember_When_PostDtoIsValid() throws Exception {
    //given
    MemberDto.Post postDto = MemberDto.Post.builder()
      .email("test@example.com")
      .nickname("jPark")
      .password("1q2w3e4r!")
      .build();
    
    String requestBodyData = objectMapper.writeValueAsString(postDto);
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .post("/members")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBodyData)
    );
    
    Member findMember = memberRepository.findByEmail("test@example.com").orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(findMember.getEmail()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value(findMember.getNickname()))
      .andDo(MockMvcResultHandlers.print());
  }
  
  @Test
  public void Should_ReturnMyPageResponse_When_AuthenticationSuccess() throws Exception {
    //given
    Member findMember = memberRepository.findByEmail("test@naver.com").orElseThrow();
    
    String accessToken = jwtProvider.generateAccessToken(findMember);
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .get("/members/mypage")
        .header("Authorization", "Bearer " + accessToken)
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.id").value(5L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.email").value("testttt@gmail.com"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.nickname").value("아차몽"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.profileImg").value("https://chamongbucket.s3.ap-northeast-2.amazonaws.com/images/member_image/1680481758976_image/jpeg"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.about").value("자기 소개를 작성 해보세요."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.carName").value("쏘나타"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.oilInfo").value("휘발유"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].id").value(38L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].memo").value("천안 도로 어딘가"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].keywords[0]").value("#편의점"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].keywords[1]").value("#체험활동"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].keywords[2]").value("#와이파이"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].placeImg").value("https://chamongbucket.s3.ap-northeast-2.amazonaws.com/images/default_image/camping%28default%29.jpg"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].isShared").value(false))
      .andExpect(MockMvcResultMatchers.jsonPath("$.visitedPlaceInfos[0].id").value(52L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.visitedPlaceInfos[0].memberId").value(5L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.visitedPlaceInfos[0].contentId").value(3207L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.visitedPlaceInfos[0].facltNm").value("남이섬 마리나 카라반"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.visitedPlaceInfos[0].lineIntro").value("자라섬 북쪽 북한강변에 위치한 침대형 원룸형 정박형 카라반 캠핑장 입니다."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.writtenArticleInfos[0].id").value(6L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.writtenArticleInfos[0].title").value("차박지 추천해주세요"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.writtenArticleInfos[0].content").value("지역 상관없이 괜찮았던 차박지 추천해주세요!"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.writtenArticleInfos[0].nickname").value("아차몽"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.writtenArticleInfos[0].comments[0].content").value("저는 개인적으로 수복캠핑장 추천드립니다!!!"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.commentedArticleInfos[0].id").value(23L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.commentedArticleInfos[0].title").value("추천해주세요"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.commentedArticleInfos[0].nickname").value("아차몽"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.commentedArticleInfos[0].viewCnt").value(4))
      .andExpect(MockMvcResultMatchers.jsonPath("$.commentedArticleInfos[0].likeCnt").value(0))
      .andExpect(MockMvcResultMatchers.jsonPath("$.likedArticleInfos[0].id").value(6L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.likedArticleInfos[0].title").value("차박지 추천해주세요"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.likedArticleInfos[0].content").value("지역 상관없이 괜찮았던 차박지 추천해주세요!"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.likedArticleInfos[0].nickname").value("아차몽"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.likedArticleInfos[0].viewCnt").value(59))
      .andDo(MockMvcResultHandlers.print())
      ;
  }
  
  @Test
  public void Should_PatchMember_When_AuthenticationSuccess() throws Exception {
    //given
    String accessToken = provideAccessToken()[1];
    
    MemberDto.Patch patchDto = MemberDto.Patch.builder()
      .nickname("이차몽")
      .about("반갑습니다.")
      .carName("아반떼")
      .oilInfo("가솔린")
      .build();
    
    String requestBodyData = objectMapper.writeValueAsString(patchDto);
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .multipart(HttpMethod.PATCH, "/members")
        .file(new MockMultipartFile("memberUpdate", "memberUpdate", MediaType.APPLICATION_JSON_VALUE,
          requestBodyData.getBytes(StandardCharsets.UTF_8)))
        .file(new MockMultipartFile("profileImg", "profileImg.jpg", MediaType.IMAGE_JPEG_VALUE,
          new FileInputStream("C:\\Users\\Jinu_Park\\Desktop\\profileImg.jpg")))
        .header("Authorization", "Bearer " + accessToken)
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("이차몽"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.about").value("반갑습니다."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.carName").value("아반떼"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.oilInfo").value("가솔린"))
      .andDo(MockMvcResultHandlers.print());
  }
  
  @Test
  public void Should_DeleteMember_When_AuthenticationSuccess() throws Exception {
    //given
    String accessToken = provideAccessToken()[1];
    
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .delete("/members")
        .header("Authorization", "Bearer " + accessToken)
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(MockMvcResultMatchers.content().string("정상적으로 회원 탈퇴 되었습니다."));
  }
  
  @Test
  public void Should_Logout_When_AuthenticationSuccess() throws Exception {
    String accessToken = provideAccessToken()[1];
    
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .get("/members/logout")
        .header("Authorization", "Bearer " + accessToken)
    );
    
    boolean isLogout = redisRepository.findBy(accessToken).startsWith("logout");
    
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().string("정상적으로 로그아웃 되었습니다."));
    
    Assertions.assertTrue(isLogout);
  }
  
  @Test
  public void Should_ReissueAccessToken_When_RefreshTokenIsValid() throws Exception {
    String refreshToken = provideAccessToken()[0];
    
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .get("/members/token")
        .header("Refresh", refreshToken)
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.header().exists("Authorization"));
  }
  
  public String[] provideAccessToken(){
    MemberDto.Post postDto = MemberDto.Post.builder()
      .email("test@example.com")
      .nickname("jPark")
      .password("1q2w3e4r!")
      .build();
    
    memberService.saveMember(postDto);
    
    Member findMember = memberService.findByEmail(postDto.getEmail());
    
    String refreshToken = jwtProvider.generateRefreshToken(findMember);
    String accessToken = jwtProvider.generateAccessToken(findMember);
    
    redisRepository.save(postDto.getEmail(), refreshToken);
    
    return new String[] {refreshToken, accessToken};
  }
}
