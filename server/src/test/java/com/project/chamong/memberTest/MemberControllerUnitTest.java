package com.project.chamong.memberTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.auth.config.SecurityConfiguration;
import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.auth.dto.LoginDto;
import com.project.chamong.auth.jwt.JwtProvider;
import com.project.chamong.auth.repository.TokenRedisRepository;
import com.project.chamong.auth.service.UserDetailsServiceImpl;
import com.project.chamong.auth.utils.CustomAuthorityUtils;
import com.project.chamong.config.WithAuthorizedMemberDto;
import com.project.chamong.member.controller.MemberController;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.member.service.MemberService;
import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.dto.VisitedPlaceDto;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;


@WebMvcTest(value = MemberController.class, includeFilters = {
  @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class),
  @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtProvider.class),
  @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CustomAuthorityUtils.class)
})
@MockBean(JpaMetamodelMappingContext.class)
public class MemberControllerUnitTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private CustomAuthorityUtils customAuthorityUtils;
  @MockBean
  private MemberService memberService;
  @MockBean
  private TokenRedisRepository redisRepository;
  @MockBean
  private MemberRepository memberRepository;
  @MockBean
  private S3Service s3Service;
  @MockBean
  private UserDetailsServiceImpl userDetailsService;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtProvider jwtProvider;
  
  @Test
  public void Should_HandleMethodArgumentNotValidException_When_EmailIsNotValid() throws Exception {
    //given
    MemberDto.Post postDto = MemberDto.Post.builder()
      .email("invalid-email")
      .nickname("jPark")
      .password("1q2w3e4r!")
      .build();

    String requestBodyData = objectMapper.writeValueAsString(postDto);

    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .post("/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBodyData)
        .with(SecurityMockMvcRequestPostProcessors.csrf())
    );

    actions
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("email"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].rejectedValue").value("invalid-email"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].reason").value("유효하지 않은 이메일 형식입니다."))
      .andDo(MockMvcResultHandlers.print());
  }
  
//  @Test
//  @WithAuthorizedMemberDto(id = 1L, email = "a123@naver.com", roles = "ROLE_USER")
//  public void Should_HandleMethodArgumentNotValidException_When_EmailNotValid() throws Exception {
//    //given
//    MemberDto.Post postDto = MemberDto.Post.builder()
//      .email("invalid-email")
//      .nickname("jPark")
//      .password("wls5177!!")
//      .build();
//
//    String requestBodyData = objectMapper.writeValueAsString(postDto);
//
//    ResultActions actions = mockMvc.perform(
//      MockMvcRequestBuilders
//        .post("/members")
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(requestBodyData)
//        .with(SecurityMockMvcRequestPostProcessors.csrf())
//    );
//
//    actions
//      .andExpect(MockMvcResultMatchers.status().isBadRequest())
//      .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
//      .andDo(MockMvcResultHandlers.print())
//    ;
//  }
  
  @Test
  public void Should_HandleMethodArgumentNotValidException_When_NicknameIsNotValid() throws Exception {
    //given
    MemberDto.Post postDto = MemberDto.Post.builder()
      .email("test@gmail.com")
      .nickname("jPark".repeat(5))
      .password("1q2w3e4r!")
      .build();
    
    String requestBodyData = objectMapper.writeValueAsString(postDto);
    
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .post("/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBodyData)
        .with(SecurityMockMvcRequestPostProcessors.csrf())
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("nickname"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].rejectedValue").value("jPark".repeat(5)))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].reason").value("nickname 길이는 최대 20자 이하로 입력해 주세요."))
      .andDo(MockMvcResultHandlers.print());
  }
  
  @Test
  public void Should_HandleMethodArgumentNotValidException_When_PasswordIsNotValid() throws Exception {
    //given
    MemberDto.Post postDto = MemberDto.Post.builder()
      .email("test@gmail.com")
      .nickname("jPark")
      .password("abc")
      .build();
    
    String requestBodyData = objectMapper.writeValueAsString(postDto);
    
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .post("/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBodyData)
        .with(SecurityMockMvcRequestPostProcessors.csrf())
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("password"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].rejectedValue").value("abc"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].reason")
        .value("password 길이는 최소 8자 이상 최대 20자 이하, 숫자 1자 이상, 대소문자 구분없이 영문자 1자 이상, 특수문자 1자 이상 입력 해주세요."))
      .andDo(MockMvcResultHandlers.print());
  }
  
  @Test
  public void Should_SaveMember_When_PostDtoIsValid() throws Exception {
    //given
    MemberDto.Post postDto = MemberDto.Post.builder()
      .email("test@gmail.com")
      .nickname("jPark")
      .password("1q2w3e4r!")
      .build();
    
    MemberDto.Response response = MemberDto.Response.builder()
      .email(postDto.getEmail())
      .nickname(postDto.getNickname())
      .build();
    
    String requestBodyData = objectMapper.writeValueAsString(postDto);
    
    BDDMockito.given(memberService.saveMember(Mockito.any(MemberDto.Post.class))).willReturn(response);
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .post("/members")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBodyData)
        .with(SecurityMockMvcRequestPostProcessors.csrf())
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@gmail.com"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("jPark"))
      .andDo(MockMvcResultHandlers.print());
  }
  
  @Test
  @WithAuthorizedMemberDto(id = 1L, email = "a123@naver.com", roles = "ROLE_USER")
  public void Should_ReturnMyPageResponse_When_AuthenticationSuccess() throws Exception {
    //given
    MemberDto.Response memberResponseDto = MemberDto.Response.builder()
      .id(1L)
      .email("a123@naver.com")
      .nickname("Jpark")
      .profileImg("abc.jpg")
      .about("반갑습니다.")
      .carName("Avante")
      .oilInfo("Gasoline")
      .build();
    
    List<MyPlaceDto.Response> myPlaceResponseDtos = List.of(MyPlaceDto.Response.builder()
      .id(1L)
      .memo("좋은 차박지")
      .keywords(List.of("추천", "좋음"))
      .placeImg("aaa.jpg")
      .isShared(true)
      .build()
    );
    
    List<VisitedPlaceDto.Response> visitedPlaceResponseDtos = List.of(VisitedPlaceDto.Response.builder()
      .id(1L)
      .memberId(1L)
      .contentId(1L)
      .facltNm("(주)디노담양힐링파크 지점")
      .lineIntro("담양 힐링파크에서 일상 속 쌓인 스트레스를 풀어보자")
      .build()
    );
    
    List<ArticleDto.Response> writtenArticleResponseDtos = List.of(ArticleDto.Response.builder()
      .id(1L)
      .title("차박지 추천")
      .content("차박지 추천합니다.")
      .nickname("Jpark")
      .articleImg("aaa.jpg")
      .build()
    );
    
    List<ArticleDto.Response> commentedArticleResponseDtos = List.of(ArticleDto.Response.builder()
      .id(1L)
      .title("차박지 추천")
      .content("차박지 추천합니다.")
      .nickname("Jpark")
      .articleImg("aaa.jpg")
      .build()
    );
    
    List<ArticleDto.Response> likedArticleResponseDtos = List.of(ArticleDto.Response.builder()
      .id(1L)
      .title("차박지 추천")
      .content("차박지 추천합니다.")
      .nickname("Jpark")
      .articleImg("aaa.jpg")
      .build()
    );
    
    MemberDto.MyPageResponse response = MemberDto.MyPageResponse.builder()
      .memberInfo(memberResponseDto)
      .myPlaceInfos(myPlaceResponseDtos)
      .visitedPlaceInfos(visitedPlaceResponseDtos)
      .writtenArticleInfos(writtenArticleResponseDtos)
      .commentedArticleInfos(commentedArticleResponseDtos)
      .likedArticleInfos(likedArticleResponseDtos)
      .build();
    
    BDDMockito.given(memberService.findMyPage(Mockito.any(AuthorizedMemberDto.class))).willReturn(response);
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .get("/members/mypage")
        .with(SecurityMockMvcRequestPostProcessors.csrf())
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.email").value("a123@naver.com"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.nickname").value("Jpark"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.profileImg").value("abc.jpg"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.about").value("반갑습니다."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.carName").value("Avante"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.memberInfo.oilInfo").value("Gasoline"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].memo").value("좋은 차박지"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].keywords[0]").value("추천"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].keywords[1]").value("좋음"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].placeImg").value("aaa.jpg"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.myPlaceInfos[0].isShared").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.visitedPlaceInfos[0].id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.visitedPlaceInfos[0].memberId").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.visitedPlaceInfos[0].contentId").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.visitedPlaceInfos[0].facltNm").value("(주)디노담양힐링파크 지점"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.visitedPlaceInfos[0].lineIntro").value("담양 힐링파크에서 일상 속 쌓인 스트레스를 풀어보자"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.writtenArticleInfos[0].id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.writtenArticleInfos[0].title").value("차박지 추천"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.writtenArticleInfos[0].content").value("차박지 추천합니다."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.writtenArticleInfos[0].nickname").value("Jpark"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.writtenArticleInfos[0].articleImg").value("aaa.jpg"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.commentedArticleInfos[0].id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.commentedArticleInfos[0].title").value("차박지 추천"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.commentedArticleInfos[0].content").value("차박지 추천합니다."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.commentedArticleInfos[0].nickname").value("Jpark"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.commentedArticleInfos[0].articleImg").value("aaa.jpg"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.likedArticleInfos[0].id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.likedArticleInfos[0].title").value("차박지 추천"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.likedArticleInfos[0].content").value("차박지 추천합니다."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.likedArticleInfos[0].nickname").value("Jpark"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.likedArticleInfos[0].articleImg").value("aaa.jpg"))
      .andDo(MockMvcResultHandlers.print());
    
  }
  
  @Test
  @WithAuthorizedMemberDto(id = 1L, email = "a123@naver.com", roles = "ROLE_USER")
  public void Should_PatchMember_When_AuthenticationSuccess() throws Exception {
    //given
    MemberDto.Patch patchDto = MemberDto.Patch.builder()
      .nickname("ABC")
      .about("반갑습니다")
      .carName("Avante")
      .oilInfo("Gasoline")
      .build();
    
    MemberDto.Response response = MemberDto.Response.builder()
      .nickname(patchDto.getNickname())
      .about(patchDto.getAbout())
      .carName(patchDto.getCarName())
      .oilInfo(patchDto.getOilInfo())
      .build();
    
    String patchDtoJson = objectMapper.writeValueAsString(patchDto);
    
    MockMultipartFile profileImgFile = new MockMultipartFile(
      "profileImg"
      , "profileImg.jpg"
      , "image/jpeg"
      , new FileInputStream("C:\\Users\\Jinu_Park\\Desktop\\profileImg.jpg")
    );
    
    MockMultipartFile patchDtoFile = new MockMultipartFile(
      "memberUpdate",
      "memberUpdate",
      "application/json",
      patchDtoJson.getBytes(StandardCharsets.UTF_8)
    );
    
    BDDMockito.given(memberService.updateMember(
      Mockito.any(MemberDto.Patch.class)
      , Mockito.any(MultipartFile.class)
      , Mockito.any(AuthorizedMemberDto.class)
      )).willReturn(response);
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .multipart(HttpMethod.PATCH,"/members")
        .file(profileImgFile)
        .file(patchDtoFile)
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .with(SecurityMockMvcRequestPostProcessors.csrf())
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value(response.getNickname()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.about").value(response.getAbout()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.carName").value(response.getCarName()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.oilInfo").value(response.getOilInfo()))
      .andDo(MockMvcResultHandlers.print());
    
  }
  
  @Test
  @WithAuthorizedMemberDto(id = 1L, email = "a123@naver.com", roles = "ROLE_USER")
  public void Should_DeleteMember_When_AuthenticationSuccess() throws Exception {
    //given
    BDDMockito.doNothing().when(memberService).deleteMember(Mockito.any(AuthorizedMemberDto.class));
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .delete("/members")
        .with(SecurityMockMvcRequestPostProcessors.csrf())
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(MockMvcResultMatchers.content().string("정상적으로 회원 탈퇴 되었습니다."))
      .andDo(MockMvcResultHandlers.print());
  }
  
  @Test
  @WithAuthorizedMemberDto(id = 1L, email = "a123@naver.com", roles = "ROLE_USER")
  public void Should_Logout_When_AuthenticationSuccess() throws Exception {
    //given
    BDDMockito.doNothing().when(memberService).logout(Mockito.any(HttpServletRequest.class), Mockito.any(AuthorizedMemberDto.class));
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .get("/members/logout")
        .with(SecurityMockMvcRequestPostProcessors.csrf())
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().string("정상적으로 로그아웃 되었습니다."))
      .andDo(MockMvcResultHandlers.print());
  }
  
  @Test
  public void Should_ProvideJWT_When_LoginSucceeds() throws Exception {
    //given
    LoginDto loginDto = LoginDto.builder()
      .email("test@gmail.com")
      .password("1q2w3e4r!")
      .build();
    
    MemberDto.Post postDto = MemberDto.Post
      .builder()
      .email("test@gmail.com")
      .password(passwordEncoder.encode("1q2w3e4r!"))
      .roles(customAuthorityUtils.crateRoles("test@gmail.com"))
      .build();
    
    Member member = Member.createMember(postDto);
    String requestBodyData = objectMapper.writeValueAsString(loginDto);
    BDDMockito.doNothing().when(redisRepository).save(Mockito.anyString(), Mockito.anyString());
    BDDMockito.given(this.userDetailsService.loadUserByUsername(Mockito.anyString())).willReturn(
      new UserDetailsServiceImpl(memberService, customAuthorityUtils).new UserDetailsImpl(member));
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .post("/members/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBodyData)
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.header().exists("Authorization"))
      .andExpect(MockMvcResultMatchers.header().exists("Refresh"))
      .andDo(MockMvcResultHandlers.print());
    
  }
  
  @Test
  public void Should_ReissueAccessToken_When_RefreshTokenIsValid() throws Exception {
    //given
    MemberDto.Post postDto = MemberDto.Post.builder()
      .email("test@gmail.com")
      .password(passwordEncoder.encode("1q2w3e4r!"))
      .roles(customAuthorityUtils.crateRoles("test@gmail.com"))
      .build();
    
    Member member = Member.createMember(postDto);
    String refreshToken = jwtProvider.generateRefreshToken(member);
    
    BDDMockito.given(redisRepository.findBy(Mockito.anyString())).willReturn(refreshToken);
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(member));
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .get("/members/token")
        .header("Refresh", refreshToken)
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.header().exists("Authorization"))
      .andDo(MockMvcResultHandlers.print());
  }
}
