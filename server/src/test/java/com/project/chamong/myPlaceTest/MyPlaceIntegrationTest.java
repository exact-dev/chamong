package com.project.chamong.myPlaceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chamong.auth.jwt.JwtProvider;
import com.project.chamong.auth.utils.CustomAuthorityUtils;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.entity.MyPlace;
import com.project.chamong.place.repository.MyPlaceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.annotation.Nullable;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MyPlaceIntegrationTest {
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private CustomAuthorityUtils customAuthorityUtils;
  
  @Autowired
  private JwtProvider jwtProvider;
  
  @Autowired
  private MemberRepository memberRepository;
  
  @Autowired
  private MyPlaceRepository myPlaceRepository;
  
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
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .multipart(HttpMethod.POST, "/pick-places")
        .file(mockBodyData)
        .file(mockPlaceImg)
        .header("Authorization", "Bearer " + generateAccessToken(null))
    );
    
    //then
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
  public void Should_GetMyPlace_When_AuthenticationSuccess_And_MemberExists() throws Exception {
    Member member = Member.createMember(
      MemberDto.Post.builder()
        .email("test@example.com")
        .nickname("jPark")
        .password("1q2w3e4r!")
        .roles(customAuthorityUtils.crateRoles("test@example.com"))
        .build()
    );
    
    MyPlace myplace = MyPlace.createMyplace(
      MyPlaceDto.Post.builder()
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build()
    );
    myplace.setMember(member);
    
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .get("/pick-places/member")
        .header("Authorization", "Bearer " + generateAccessToken(member))
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].memo").value("한강"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].keywords[0]").value("#물놀이"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].keywords[1]").value("#낚시"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].keywords[2]").value("#강"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].mapX").value(126.95810162985441))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].mapY").value(37.51784937044093))
      .andDo(MockMvcResultHandlers.print());
    
  }
  
  @Test
  public void Should_GetMyPlace_For_All_Users() throws Exception {
    MyPlace myplace = MyPlace.createMyplace(
      MyPlaceDto.Post.builder()
        .memo("한강")
        .keywords(List.of("#물놀이", "#낚시", "#강"))
        .mapX(126.95810162985441)
        .mapY(37.51784937044093)
        .build()
    );
    
    myplace.setIsShared(true);
    
    myPlaceRepository.save(myplace);
    
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .get("/pick-places/shared")
    );
    
    String jsonResponse = actions.andReturn().getResponse().getContentAsString();
    
    MyPlaceDto.Response response = Arrays.stream(objectMapper.readValue(jsonResponse, MyPlaceDto.Response[].class))
      .filter(responseDto -> responseDto.getId() == myplace.getId()).findAny().get();
    
    actions.andExpect(MockMvcResultMatchers.status().isOk());
    
    Assertions.assertThat("한강").isEqualTo(response.getMemo());
    Assertions.assertThat("#물놀이").isEqualTo(response.getKeywords().get(0));
    Assertions.assertThat("#낚시").isEqualTo(response.getKeywords().get(1));
    Assertions.assertThat("#강").isEqualTo(response.getKeywords().get(2));
    Assertions.assertThat(126.95810162985441).isEqualTo(response.getMapX());
    Assertions.assertThat(37.51784937044093).isEqualTo(response.getMapY());
  }
  
  @Test
  public void Should_PatchMyPlace_When_AuthenticationSuccess_And_HavePermission() throws Exception {
    //given
    MyPlace myplace = MyPlace.createMyplace(MyPlaceDto.Post
      .builder()
      .memo("바다")
      .keywords(List.of("#물놀이", "#낚시", "#바다"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build()
    );
    
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
    
    myplace.setMember(member);
    
    String accessToken = generateAccessToken(member);
    
    //when
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .multipart(HttpMethod.PATCH, "/pick-places/" + myplace.getId())
        .file(mockPlaceImg)
        .file(requestBody)
        .header("Authorization", "Bearer " + accessToken)
    );
    
    //then
    actions
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.memo").value("한강"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[0]").value("#물놀이"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[1]").value("#낚시"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[2]").value("#강"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.mapX").value(126.95810162985441))
      .andExpect(MockMvcResultMatchers.jsonPath("$.mapY").value(37.51784937044093))
      .andDo(MockMvcResultHandlers.print());
  }
  
  @Test
  public void Should_DeleteMyPlace_When_AuthenticationSuccess_And_HavePermission() throws Exception {
    MyPlace myplace = MyPlace.createMyplace(MyPlaceDto.Post
      .builder()
      .memo("바다")
      .keywords(List.of("#물놀이", "#낚시", "#바다"))
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
    
    myplace.setMember(member);
    
    String accessToken = generateAccessToken(member);
    
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .delete("/pick-places/" + myplace.getId())
        .header("Authorization", "Bearer " + accessToken)
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(MockMvcResultMatchers.content().string("등록한 차박지가 정상적으로 삭제 되었습니다."));
  }
  
  @Test
  public void Should_Handle_BusinessLogicException_When_Have_NoPermission() throws Exception {
    MyPlace myplace = MyPlace.createMyplace(MyPlaceDto.Post
      .builder()
      .memo("바다")
      .keywords(List.of("#물놀이", "#낚시", "#바다"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build()
    );
    
    Member member = Member.createMember(
      MemberDto.Post.builder()
        .email("test2@example.com")
        .nickname("jPark")
        .password("1q2w3e4r!")
        .roles(customAuthorityUtils.crateRoles("test@example.com"))
        .build());
    
    myplace.setMember(member);
    
    myPlaceRepository.save(myplace);
    
    String accessToken = generateAccessToken(null);
    
    ResultActions actions = mockMvc.perform(
      MockMvcRequestBuilders
        .delete("/pick-places/" + myplace.getId())
        .header("Authorization", "Bearer " + accessToken)
    );
    
    actions
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(MockMvcResultMatchers.jsonPath("$.message")
        .value("나만의 차박지를 수정, 삭제할 권한이 없습니다. 등록자 본인만 수정, 삭제가 가능 합니다."))
      .andDo(MockMvcResultHandlers.print());
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
