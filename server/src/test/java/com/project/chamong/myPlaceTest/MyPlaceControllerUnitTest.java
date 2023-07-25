package com.project.chamong.myPlaceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chamong.auth.config.SecurityConfiguration;
import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.auth.jwt.JwtProvider;
import com.project.chamong.auth.repository.TokenRedisRepository;
import com.project.chamong.auth.utils.CustomAuthorityUtils;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.place.controller.MyPlaceController;
import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.service.MyPlaceService;
import com.project.chamong.s3.service.S3Service;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@WebMvcTest(value = MyPlaceController.class, includeFilters = {
  @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class),
  @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtProvider.class),
  @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CustomAuthorityUtils.class)
})
@MockBean(JpaMetamodelMappingContext.class)
public class MyPlaceControllerUnitTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private JwtProvider jwtProvider;
  
  @Autowired
  private CustomAuthorityUtils customAuthorityUtils;
  
  @MockBean
  private MyPlaceService myPlaceService;
  
  @MockBean
  private TokenRedisRepository redisRepository;
  
  @MockBean
  private MemberRepository memberRepository;
  
  @MockBean
  private S3Service s3Service;
  
  @Test
  public void Should_SaveMyPlace_When_AuthenticationSuccess_And_PostDtoIsValid() throws Exception {
    //given
    MyPlaceDto.Post postDto = MyPlaceDto.Post
      .builder()
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build();
    
    MyPlaceDto.Response response = MyPlaceDto.Response
      .builder()
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build();
    
    Member member = Member.createMember(
      MemberDto.Post.builder()
        .email("test@example.com")
        .nickname("jPark")
        .password("1q2w3e4r!")
        .roles(customAuthorityUtils.crateRoles("test@example.com"))
        .build());
    
    String requestBodyData = objectMapper.writeValueAsString(postDto);
    
    MockMultipartFile mockBodyData = new MockMultipartFile(
      "postMyPlace",
      "postMyPlace",
      MediaType.APPLICATION_JSON_VALUE,
      requestBodyData.getBytes(StandardCharsets.UTF_8)
    );
    
    MockMultipartFile mockPlaceImg = new MockMultipartFile(
      "placeImg",
      "place.jpg",
      MediaType.IMAGE_JPEG_VALUE,
      new FileInputStream("C:\\Users\\Jinu_Park\\Desktop\\place.jpg")
    );
    
    BDDMockito.given(myPlaceService.saveMyPlace(
      Mockito.any(MyPlaceDto.Post.class), Mockito.any(AuthorizedMemberDto.class), Mockito.any(MultipartFile.class))
    ).willReturn(response);
    
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(member));
    
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .multipart(HttpMethod.POST, "/pick-places")
        .file(mockBodyData)
        .file(mockPlaceImg)
        .header("Authorization", "Bearer " + jwtProvider.generateAccessToken(member))
        .contentType(MediaType.MULTIPART_FORM_DATA)
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.memo").value("한강"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[0]").value("#물놀이"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[1]").value("#낚시"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[2]").value("#강"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.mapX").value(126.95810162985441))
      .andExpect(MockMvcResultMatchers.jsonPath("$.mapY").value(37.51784937044093));
  }
  
  @Test
  public void Should_HandleArgumentNotValidException_When_PostDtoIsNotValid() throws Exception {
    //given
    MyPlaceDto.Post postDto = MyPlaceDto.Post
      .builder()
      .memo("")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build();
    
    Member member = Member.createMember(
      MemberDto.Post.builder()
        .email("test@example.com")
        .nickname("jPark")
        .password("1q2w3e4r!")
        .roles(customAuthorityUtils.crateRoles("test@example.com"))
        .build());
    
    String requestBodyData = objectMapper.writeValueAsString(postDto);
    
    MockMultipartFile mockBodyData = new MockMultipartFile(
      "postMyPlace",
      "postMyPlace",
      MediaType.APPLICATION_JSON_VALUE,
      requestBodyData.getBytes(StandardCharsets.UTF_8)
    );
    
    MockMultipartFile mockPlaceImg = new MockMultipartFile(
      "placeImg",
      "place.jpg",
      MediaType.IMAGE_JPEG_VALUE,
      new FileInputStream("C:\\Users\\Jinu_Park\\Desktop\\place.jpg")
    );
    
    BDDMockito.given(myPlaceService.saveMyPlace(
      Mockito.any(MyPlaceDto.Post.class), Mockito.any(AuthorizedMemberDto.class), Mockito.any(MultipartFile.class))
    ).willReturn(MyPlaceDto.Response.builder().build());
    
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(member));
    
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .multipart(HttpMethod.POST, "/pick-places")
        .file(mockBodyData)
        .file(mockPlaceImg)
        .header("Authorization", "Bearer " + jwtProvider.generateAccessToken(member))
        .contentType(MediaType.MULTIPART_FORM_DATA)
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("memo"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].rejectedValue").value(""))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].reason").value("메모는 공백일 수 없습니다."));
  }
  
  @Test
  public void Should_getMyPlace_When_AuthenticationSuccess() throws Exception {
    //given
    List<MyPlaceDto.Response> responses = List.of( MyPlaceDto.Response
      .builder()
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build()
    );
    
    Member member = Member.createMember(
      MemberDto.Post.builder()
        .email("test@example.com")
        .nickname("jPark")
        .password("1q2w3e4r!")
        .roles(customAuthorityUtils.crateRoles("test@example.com"))
        .build());
    
    
    BDDMockito.given(myPlaceService.findMyPlaceByMember(Mockito.any(AuthorizedMemberDto.class))).willReturn(responses);
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(member));
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .get("/pick-places/member")
        .header("Authorization", "Bearer " + jwtProvider.generateAccessToken(member))
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].memo").value("한강"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].keywords[0]").value("#물놀이"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].keywords[1]").value("#낚시"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].keywords[2]").value("#강"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].mapX").value(126.95810162985441))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].mapY").value(37.51784937044093));
      
  }
  
  @Test
  public void Should_getMyPlaceIsShared_For_AllUsers() throws Exception {
    //given
    List<MyPlaceDto.Response> responses = List.of( MyPlaceDto.Response
      .builder()
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build()
    );
    
    BDDMockito.given(myPlaceService.findMyPlaceByIsShared()).willReturn(responses);
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .get("/pick-places/shared")
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].memo").value("한강"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].keywords[0]").value("#물놀이"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].keywords[1]").value("#낚시"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].keywords[2]").value("#강"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].mapX").value(126.95810162985441))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].mapY").value(37.51784937044093));
  }
  
  @Test
  public void Should_PatchMyPlace_When_AuthenticationSuccess() throws Exception {
    MyPlaceDto.Response response = MyPlaceDto.Response
      .builder()
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build();
    
    MyPlaceDto.Patch patchDto = MyPlaceDto.Patch.builder()
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .isShared(true)
      .build();
    
    Member member = Member.createMember(
      MemberDto.Post.builder()
        .email("test@example.com")
        .nickname("jPark")
        .password("1q2w3e4r!")
        .roles(customAuthorityUtils.crateRoles("test@example.com"))
        .build());
    
    MockMultipartFile mockPlaceImg = new MockMultipartFile(
      "placeImg",
      "place.jpg",
      MediaType.IMAGE_JPEG_VALUE,
      new FileInputStream("C:\\Users\\Jinu_Park\\Desktop\\place.jpg")
    );
    
    String requestBodyData = objectMapper.writeValueAsString(patchDto);
    
    MockMultipartFile requestBody = new MockMultipartFile(
      "patchMyPlace",
      "patchMyPlace",
      MediaType.APPLICATION_JSON_VALUE,
      requestBodyData.getBytes(StandardCharsets.UTF_8)
    );
    
    BDDMockito.given(myPlaceService.updateMyPlace(
      Mockito.anyLong(), Mockito.any(MyPlaceDto.Patch.class), Mockito.any(AuthorizedMemberDto.class), Mockito.any(MultipartFile.class)))
      .willReturn(response);
    
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(member));
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .multipart(HttpMethod.PATCH, "/pick-places/1")
        .file(requestBody)
        .file(mockPlaceImg)
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .header("Authorization", "Bearer " + jwtProvider.generateAccessToken(member))
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.memo").value("한강"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[0]").value("#물놀이"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[1]").value("#낚시"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[2]").value("#강"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.mapX").value(126.95810162985441))
      .andExpect(MockMvcResultMatchers.jsonPath("$.mapY").value(37.51784937044093));
  }
  
  @Test
  public void Should_DeleteMyPlace_When_AuthenticationSuccess() throws Exception {
    //given
    Member member = Member.createMember(
      MemberDto.Post.builder()
        .email("test@example.com")
        .nickname("jPark")
        .password("1q2w3e4r!")
        .roles(customAuthorityUtils.crateRoles("test@example.com"))
        .build());
    
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(member));
    BDDMockito.doNothing().when(myPlaceService).deleteMyPlace(Mockito.anyLong(), Mockito.any(AuthorizedMemberDto.class));
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .delete("/pick-places/1")
        .header("Authorization", "Bearer " + jwtProvider.generateAccessToken(member))
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(MockMvcResultMatchers.content().string("등록한 차박지가 정상적으로 삭제 되었습니다."));
    
    BDDMockito.verify(myPlaceService, VerificationModeFactory.times(1))
      .deleteMyPlace(Mockito.anyLong(), Mockito.any(AuthorizedMemberDto.class));
    
  }
  
  @Test
  public void Should_Handle_ConstraintViolationException_When_PathVariable_Is_Negative() throws Exception {
    //given
    Member member = Member.createMember(
      MemberDto.Post.builder()
        .email("test@example.com")
        .nickname("jPark")
        .password("1q2w3e4r!")
        .roles(customAuthorityUtils.crateRoles("test@example.com"))
        .build());
    
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(member));
    BDDMockito.doNothing().when(myPlaceService).deleteMyPlace(Mockito.anyLong(), Mockito.any(AuthorizedMemberDto.class));
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .delete("/pick-places/-1")
        .header("Authorization", "Bearer " + jwtProvider.generateAccessToken(member))
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors[0].propertyPath").value("deleteMyPlace.id"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors[0].rejectedValue").value("-1"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors[0].reason").value("must be greater than 0"));
  }
  
}
