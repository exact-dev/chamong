package com.project.chamong.visitedPlaceTest;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.camping.entity.Content;
import com.project.chamong.camping.service.CampingApiService;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.service.MemberService;
import com.project.chamong.place.dto.VisitedPlaceDto;
import com.project.chamong.place.entity.VisitedPlace;
import com.project.chamong.place.mapper.VisitedPlaceMapper;
import com.project.chamong.place.repository.VisitedPlaceRepository;
import com.project.chamong.place.service.VisitedPlaceService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class VisitedPlaceServiceUnitTest {
  
  @Mock
  private MemberService memberService;
  
  @Mock
  private VisitedPlaceRepository visitedPlaceRepository;
  
  @Mock
  private CampingApiService campingApiService;
  
  @Mock
  private VisitedPlaceMapper mapper;
  
  @InjectMocks
  private VisitedPlaceService visitedPlaceService;
  
  @Test
  public void Should_SaveVisitedPlace_When_VisitedPlace_Does_Not_Exist_And_MemberExists(){
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto.builder()
      .email("test@example.com")
      .id(1L)
      .build();
    
    VisitedPlaceDto.Response responseDto = VisitedPlaceDto.Response
      .builder()
      .id(1L)
      .facltNm("(주)디노담양힐링파크 지점")
      .lineIntro("담양 힐링파크에서 일상 속 쌓인 스트레스를 풀어보자")
      .addr1("전남 담양군 봉산면 탄금길 9-26")
      .firstImageUrl("https://gocamping.or.kr/upload/camp/4/thumb/thumb_720_4548WQ5JCsRSkbHrBAaZylrQ.jpg")
      .build();
    
    BDDMockito.given(memberService.findByEmail(Mockito.anyString())).willReturn(Member.builder().build());
    BDDMockito.given(visitedPlaceRepository.save(Mockito.any(VisitedPlace.class))).willReturn(VisitedPlace.builder().build());
    BDDMockito.given(campingApiService.findContent(Mockito.anyLong())).willReturn(new Content());
    BDDMockito.given(mapper.visitedPlaceToResponseDto(Mockito.any(VisitedPlace.class))).willReturn(responseDto);
    
    VisitedPlaceDto.Response response = visitedPlaceService.saveVisitedPlace(1L, authorizedMemberDto);
    
    BDDMockito.verify(memberService).findByEmail(Mockito.anyString());
    BDDMockito.verify(visitedPlaceRepository).save(Mockito.any(VisitedPlace.class));
    BDDMockito.verify(campingApiService).findContent(Mockito.anyLong());
    BDDMockito.verify(mapper).visitedPlaceToResponseDto(Mockito.any(VisitedPlace.class));
    
    Assertions.assertThat(1L).isEqualTo(response.getId());
    Assertions.assertThat("(주)디노담양힐링파크 지점").isEqualTo(response.getFacltNm());
    Assertions.assertThat("담양 힐링파크에서 일상 속 쌓인 스트레스를 풀어보자").isEqualTo(response.getLineIntro());
    Assertions.assertThat("전남 담양군 봉산면 탄금길 9-26").isEqualTo(response.getAddr1());
    Assertions.assertThat("https://gocamping.or.kr/upload/camp/4/thumb/thumb_720_4548WQ5JCsRSkbHrBAaZylrQ.jpg")
      .isEqualTo(response.getFirstImageUrl());
  }
  
  @Test
  public void Should_Throw_BusinessLogicException_When_VisitedPlace_Already_Exists(){
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto.builder()
      .email("test@example.com")
      .id(1L)
      .build();
    
    Member member = Member.builder().build();
    
    Content content = new Content();
    content.setContentId(1L);
    
    VisitedPlace visitedPlace = VisitedPlace.createVisitedPlace(
      VisitedPlaceDto.Post
        .builder()
        .content(content)
        .build()
    );
    
    member.getVisitedPlaces().add(visitedPlace);
    
    BDDMockito.given(memberService.findByEmail(Mockito.anyString())).willReturn(member);
    
    org.junit.jupiter.api.Assertions.assertThrows(
      BusinessLogicException.class, () -> visitedPlaceService.saveVisitedPlace(1L, authorizedMemberDto)
    );
  }
  
  @Test
  public void Should_Delete_VisitedPlace_When_VisitedPlace_Exists_And_Have_Permission(){
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto.builder()
      .email("test@example.com")
      .id(1L)
      .build();
    
    VisitedPlace visitedPlace = VisitedPlace.createVisitedPlace(
      VisitedPlaceDto.Post
        .builder()
        .content(new Content())
        .build()
    );
    
    Member member = Member.builder().build();
    member.setId(1L);
    
    visitedPlace.setMember(member);
    
    BDDMockito.given(visitedPlaceRepository.findById(Mockito.anyLong())).willReturn(Optional.of(visitedPlace));
    BDDMockito.doNothing().when(visitedPlaceRepository).deleteById(Mockito.anyLong());
    
    visitedPlaceService.deleteVisitedPlace(1L, authorizedMemberDto);
    
    BDDMockito.verify(visitedPlaceRepository).findById(Mockito.anyLong());
    BDDMockito.verify(visitedPlaceRepository).deleteById(Mockito.anyLong());
  }
  
  @Test
  public void Should_Throw_BusinessLogicException_When_Have_NoPermission(){
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto.builder()
      .email("test@example.com")
      .id(1L)
      .build();
    
    VisitedPlace visitedPlace = VisitedPlace.createVisitedPlace(
      VisitedPlaceDto.Post
        .builder()
        .content(new Content())
        .build()
    );
    
    Member member = Member.builder().build();
    member.setId(2L);
    
    visitedPlace.setMember(member);
    
    BDDMockito.given(visitedPlaceRepository.findById(Mockito.anyLong())).willReturn(Optional.of(visitedPlace));
    
    org.junit.jupiter.api.Assertions.assertThrows(
      BusinessLogicException.class, () -> visitedPlaceService.deleteVisitedPlace(1L, authorizedMemberDto)
    );
  }
  
  @Test
  public void Should_Throw_BusinessLogicException_When_VisitedPlace_Does_Not_Exist(){
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto.builder()
      .email("test@example.com")
      .id(1L)
      .build();
    
    BDDMockito.given(visitedPlaceRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(null));
    
    org.junit.jupiter.api.Assertions.assertThrows(
      BusinessLogicException.class, () -> visitedPlaceService.deleteVisitedPlace(1L, authorizedMemberDto)
    );
  }
}
