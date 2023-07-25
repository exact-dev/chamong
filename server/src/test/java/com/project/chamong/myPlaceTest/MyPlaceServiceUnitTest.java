package com.project.chamong.myPlaceTest;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.service.MemberService;
import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.entity.MyPlace;
import com.project.chamong.place.mapper.MyPlaceMapper;
import com.project.chamong.place.repository.MyPlaceRepository;
import com.project.chamong.place.service.MyPlaceService;
import com.project.chamong.s3.service.S3Service;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MyPlaceServiceUnitTest {
  
  @Mock
  private MyPlaceRepository myPlaceRepository;
  
  @Mock
  private MemberService memberService;
  
  @Mock
  private MyPlaceMapper mapper;
  
  @Mock
  private S3Service s3Service;
  
  @InjectMocks
  private MyPlaceService myPlaceService;
  
  @Test
  public void Should_SaveMyPlace_When_MemberExist() throws IOException {
    //given
    MyPlaceDto.Post postDto = MyPlaceDto.Post
      .builder()
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build();
    
    MyPlaceDto.Response responseDto = MyPlaceDto.Response
      .builder()
      .id(1L)
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build();
    
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto.builder()
      .email("test@example.com")
      .id(1L)
      .build();
    
    MockMultipartFile mockPlaceImg = new MockMultipartFile(
      "placeImg",
      "place.jpg",
      MediaType.IMAGE_JPEG_VALUE,
      new FileInputStream("C:\\Users\\Jinu_Park\\Desktop\\place.jpg")
    );
    
    //when
    BDDMockito.given(memberService.findByEmail(Mockito.anyString())).willReturn(Member.builder().build());
    BDDMockito.given(s3Service.uploadFile(Mockito.any(MultipartFile.class), Mockito.anyString()))
      .willReturn("https://chamongbucket.s3.ap-northeast-2.amazonaws.com/images/default_image/member%28default%29.jpg");
    BDDMockito.given(myPlaceRepository.save(Mockito.any(MyPlace.class))).willReturn(MyPlace.builder().build());
    BDDMockito.given(mapper.myPlaceToResponse(Mockito.any(MyPlace.class))).willReturn(responseDto);
    
    MyPlaceDto.Response response = myPlaceService.saveMyPlace(postDto, authorizedMemberDto, mockPlaceImg);
    
    BDDMockito.verify(memberService).findByEmail(Mockito.anyString());
    BDDMockito.verify(s3Service, VerificationModeFactory.times(0)).getDefaultProfileImg();
    BDDMockito.verify(s3Service).uploadFile(Mockito.any(MultipartFile.class), Mockito.anyString());
    BDDMockito.verify(myPlaceRepository).save(Mockito.any(MyPlace.class));
    BDDMockito.verify(mapper).myPlaceToResponse(Mockito.any(MyPlace.class));
    
    Assertions.assertThat(response.getId()).isEqualTo(1L);
    Assertions.assertThat(response.getMemo()).isEqualTo("한강");
    Assertions.assertThat(response.getKeywords().get(0)).isEqualTo("#물놀이");
    Assertions.assertThat(response.getKeywords().get(1)).isEqualTo("#낚시");
    Assertions.assertThat(response.getKeywords().get(2)).isEqualTo("#강");
    Assertions.assertThat(response.getMapX()).isEqualTo(126.95810162985441);
    Assertions.assertThat(response.getMapY()).isEqualTo(37.51784937044093);
  }
  
  @Test
  public void Should_FindMyPlace_When_MemberExist(){
    //given
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto.builder()
      .email("test@example.com")
      .id(1L)
      .build();
    
    List<MyPlaceDto.Response> responseDtos = List.of(MyPlaceDto.Response
      .builder()
      .id(1L)
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build());
    
    BDDMockito.given(memberService.findByEmail(Mockito.anyString())).willReturn(Member.builder().build());
    BDDMockito.given(mapper.myPlacesToMyPlaceResponses(Mockito.anyList())).willReturn(responseDtos);
    
    //when
    List<MyPlaceDto.Response> responses = myPlaceService.findMyPlaceByMember(authorizedMemberDto);
    
    //then
    BDDMockito.verify(memberService).findByEmail(Mockito.anyString());
    BDDMockito.verify(mapper).myPlacesToMyPlaceResponses(Mockito.anyList());
    
    Assertions.assertThat(responses.get(0).getId()).isEqualTo(1L);
    Assertions.assertThat(responses.get(0).getMemo()).isEqualTo("한강");
    Assertions.assertThat(responses.get(0).getKeywords().get(0)).isEqualTo("#물놀이");
    Assertions.assertThat(responses.get(0).getKeywords().get(1)).isEqualTo("#낚시");
    Assertions.assertThat(responses.get(0).getKeywords().get(2)).isEqualTo("#강");
    Assertions.assertThat(responses.get(0).getMapX()).isEqualTo(126.95810162985441);
    Assertions.assertThat(responses.get(0).getMapY()).isEqualTo(37.51784937044093);
  }
  
  @Test
  public void Should_FindMyPlace_When_IsShared_For_AllUsers(){
    List<MyPlaceDto.Response> responseDtos = List.of(MyPlaceDto.Response
      .builder()
      .id(1L)
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build());
    
    BDDMockito.given(myPlaceRepository.findByIsSharedTrue()).willReturn(List.of(MyPlace.builder().build()));
    BDDMockito.given(mapper.myPlacesToMyPlaceResponses(Mockito.anyList())).willReturn(responseDtos);
    
    List<MyPlaceDto.Response> responses = myPlaceService.findMyPlaceByIsShared();
    
    BDDMockito.verify(myPlaceRepository).findByIsSharedTrue();
    BDDMockito.verify(mapper).myPlacesToMyPlaceResponses(Mockito.anyList());
    
    Assertions.assertThat(responses.get(0).getId()).isEqualTo(1L);
    Assertions.assertThat(responses.get(0).getMemo()).isEqualTo("한강");
    Assertions.assertThat(responses.get(0).getKeywords().get(0)).isEqualTo("#물놀이");
    Assertions.assertThat(responses.get(0).getKeywords().get(1)).isEqualTo("#낚시");
    Assertions.assertThat(responses.get(0).getKeywords().get(2)).isEqualTo("#강");
    Assertions.assertThat(responses.get(0).getMapX()).isEqualTo(126.95810162985441);
    Assertions.assertThat(responses.get(0).getMapY()).isEqualTo(37.51784937044093);
  }
  
  @Test
  public void Should_UpdateMyPlace_When_Have_Permission() throws IOException {
    //given
    MyPlaceDto.Patch patchDto = MyPlaceDto.Patch.builder()
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .isShared(true)
      .build();
    
    MyPlace myplace = MyPlace.createMyplace(
      MyPlaceDto.Post
        .builder()
        .memo("한강")
        .keywords(List.of("#물놀이", "#낚시", "#강"))
        .mapX(126.95810162985441)
        .mapY(37.51784937044093)
        .build()
    );
    
    Member member = Member.builder().build();
    member.setId(1L);
    myplace.setMember(member);
    
    MyPlaceDto.Response responseDto = MyPlaceDto.Response
      .builder()
      .id(1L)
      .memo("한강")
      .keywords(List.of("#물놀이", "#낚시", "#강"))
      .mapX(126.95810162985441)
      .mapY(37.51784937044093)
      .build();
    
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto.builder()
      .email("test@example.com")
      .id(1L)
      .build();
    
    MockMultipartFile mockPlaceImg = new MockMultipartFile(
      "placeImg",
      "place.jpg",
      MediaType.IMAGE_JPEG_VALUE,
      new FileInputStream("C:\\Users\\Jinu_Park\\Desktop\\place.jpg")
    );
    
    BDDMockito.given(myPlaceRepository.findById(Mockito.anyLong())).willReturn(Optional.of(myplace));
    BDDMockito.given(s3Service.uploadFile(Mockito.any(MultipartFile.class), Mockito.anyString()))
      .willReturn("https://chamongbucket.s3.ap-northeast-2.amazonaws.com/images/default_image/member%28default%29.jpg");
    
    BDDMockito.given(mapper.myPlaceToResponse(Mockito.any(MyPlace.class))).willReturn(responseDto);
    
    //when
    MyPlaceDto.Response response = myPlaceService.updateMyPlace(1L, patchDto, authorizedMemberDto, mockPlaceImg);
    
    BDDMockito.verify(myPlaceRepository).findById(Mockito.anyLong());
    BDDMockito.verify(s3Service).uploadFile(Mockito.any(MultipartFile.class), Mockito.anyString());
    BDDMockito.verify(mapper).myPlaceToResponse(Mockito.any(MyPlace.class));
    
    Assertions.assertThat(response.getId()).isEqualTo(1L);
    Assertions.assertThat(response.getMemo()).isEqualTo("한강");
    Assertions.assertThat(response.getKeywords().get(0)).isEqualTo("#물놀이");
    Assertions.assertThat(response.getKeywords().get(1)).isEqualTo("#낚시");
    Assertions.assertThat(response.getKeywords().get(2)).isEqualTo("#강");
    Assertions.assertThat(response.getMapX()).isEqualTo(126.95810162985441);
    Assertions.assertThat(response.getMapY()).isEqualTo(37.51784937044093);
  }
  
  @Test
  public void Should_Throw_BusinessLogicException_When_Have_NoPermission() throws IOException {
    //given
    MyPlaceDto.Patch patchDto = MyPlaceDto.Patch
      .builder()
      .build();
    
    MyPlace myplace = MyPlace.createMyplace(
      MyPlaceDto.Post
        .builder()
        .build()
    );
    
    Member member = Member.builder().build();
    member.setId(2L);
    myplace.setMember(member);
    
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto.builder()
      .email("test@example.com")
      .id(1L)
      .build();
    
    MockMultipartFile mockPlaceImg = new MockMultipartFile(
      "placeImg",
      "place.jpg",
      MediaType.IMAGE_JPEG_VALUE,
      new FileInputStream("C:\\Users\\Jinu_Park\\Desktop\\place.jpg")
    );
    
    BDDMockito.given(myPlaceRepository.findById(Mockito.anyLong())).willReturn(Optional.of(myplace));
    
    //when
    org.junit.jupiter.api.Assertions.assertThrows(
      BusinessLogicException.class, () -> myPlaceService.updateMyPlace(1L, patchDto, authorizedMemberDto, mockPlaceImg));
  }
  
  @Test
  public void Should_DeleteMyPlace_When_Have_Permission(){
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto.builder()
      .email("test@example.com")
      .id(1L)
      .build();
    
    MyPlace myplace = MyPlace.createMyplace(
      MyPlaceDto.Post
        .builder()
        .build()
    );
    
    Member member = Member.builder().build();
    member.setId(1L);
    myplace.setMember(member);
    
    BDDMockito.given(myPlaceRepository.findById(Mockito.anyLong())).willReturn(Optional.of(myplace));
    BDDMockito.doNothing().when(myPlaceRepository).deleteById(Mockito.anyLong());
    
    //when
    myPlaceService.deleteMyPlace(1L, authorizedMemberDto);
    
    //then
    BDDMockito.verify(myPlaceRepository).findById(Mockito.anyLong());
    BDDMockito.verify(myPlaceRepository).deleteById(Mockito.anyLong());
  }
}
