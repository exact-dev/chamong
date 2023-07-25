package com.project.chamong.memberTest;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.auth.repository.TokenRedisRepository;
import com.project.chamong.auth.utils.CustomAuthorityUtils;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.mapper.MemberMapper;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.member.service.MemberService;
import com.project.chamong.s3.service.S3Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MemberServiceUnitTest {
  @Mock
  private MemberRepository memberRepository;
  
  @Mock
  private TokenRedisRepository redisRepository;
  
  @Mock
  private S3Service s3Service;
  
  @Mock
  MemberMapper mapper;
  
  @Mock
  private CustomAuthorityUtils customAuthorityUtils;
  
  @Mock
  private PasswordEncoder passwordEncoder;
  
  @InjectMocks
  private MemberService memberService;
  @Test
  public void Should_SaveMember_When_MemberEmailDoesNotExist(){
    //given
    MemberDto.Post postDto = MemberDto.Post.builder()
      .email("test@gmail.com")
      .nickname("jPark")
      .password("1q2w3e4r!")
      .build();
    
//    MemberIntegrationTest member = MemberIntegrationTest.createMember(postDto);
    
    MemberDto.Response memberResponseDto = MemberDto.Response.builder()
      .id(1L)
      .email(postDto.getEmail())
      .nickname(postDto.getNickname())
      .profileImg("abc.jpg")
      .about("반갑습니다.")
      .carName("Avante")
      .oilInfo("Gasoline")
      .build();
    
    BDDMockito.given(passwordEncoder.encode(Mockito.anyString())).willReturn("{bcrypt}$2a$10$IZOKJjg1d8jzjBhdEO8aPO1ftUUvTQnAw9lCqc.iUo71uRisHFgBO");
    BDDMockito.given(s3Service.getDefaultProfileImg()).willReturn("https://chamongbucket.s3.ap-northeast-2.amazonaws.com/images/default_image/member%28default%29.jpg");
    BDDMockito.given(mapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(memberResponseDto);
    BDDMockito.given(memberRepository.save(Mockito.any(Member.class))).willReturn(Member.builder().build());
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.empty());
    
    //when
    MemberDto.Response response = memberService.saveMember(postDto);
    
    //then
    BDDMockito.verify(passwordEncoder, VerificationModeFactory.times(1)).encode(Mockito.anyString());
    BDDMockito.verify(s3Service, VerificationModeFactory.times(1)).getDefaultProfileImg();
    BDDMockito.verify(mapper, VerificationModeFactory.times(1)).memberToMemberResponseDto(Mockito.any(Member.class));
    BDDMockito.verify(memberRepository, VerificationModeFactory.times(1)).save(Mockito.any(Member.class));
    BDDMockito.verify(memberRepository, VerificationModeFactory.times(1)).findByEmail(Mockito.anyString());
    
    Assertions.assertEquals(1L, response.getId());
    Assertions.assertEquals("test@gmail.com", response.getEmail());
    Assertions.assertEquals("jPark", response.getNickname());
    Assertions.assertEquals("abc.jpg", response.getProfileImg());
    Assertions.assertEquals("반갑습니다.", response.getAbout());
    Assertions.assertEquals("Avante", response.getCarName());
    Assertions.assertEquals("Gasoline", response.getOilInfo());
  }
  
  @Test
  public void Should_ThrowBusinessLogicException_When_MemberEmailAlreadyExist(){
    //given
    MemberDto.Post postDto = MemberDto.Post
      .builder()
      .email("test@gmail.com")
      .build();
    
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(Member.builder().build()));
    
    //when, then
    Assertions.assertThrows(BusinessLogicException.class, () -> memberService.saveMember(postDto));
    
    BDDMockito.verify(memberRepository, VerificationModeFactory.times(1)).findByEmail(Mockito.anyString());
    BDDMockito.verify(memberRepository, VerificationModeFactory.times(0)).save(Mockito.any(Member.class));
  }
  
  @Test
  public void Should_ReturnMemberMyPageResponse_When_MemberEmailExist(){
    //given
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto
      .builder()
      .id(1L)
      .email("test@gmail.com")
      .build();
    
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(Member.builder().build()));
    BDDMockito.given(mapper.memberToMemberMypageResponse(Mockito.any(Member.class))).willReturn(MemberDto.MyPageResponse.builder().build());
    
    //when
    MemberDto.MyPageResponse myPageResponse = memberService.findMyPage(authorizedMemberDto);
    
    //then
    BDDMockito.verify(memberRepository, VerificationModeFactory.times(1)).findByEmail(Mockito.anyString());
    BDDMockito.verify(mapper, VerificationModeFactory.times(1)).memberToMemberMypageResponse(Mockito.any(Member.class));
  }
  
  @Test
  public void Should_UpdateMember_When_MemberEmailExist() throws IOException {
    //given
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto
      .builder()
      .id(1L)
      .email("test@gmail.com")
      .build();
    
    MemberDto.Patch patchDto = MemberDto.Patch
      .builder()
      .nickname("JPark")
      .about("Hi")
      .carName("Sonata")
      .oilInfo("Gasoline")
      .build();
    
    Member member = Member.createMember(
      MemberDto.Post
        .builder()
        .nickname("Spring")
        .build()
    );
    
    MemberDto.Response memberResponseDto = MemberDto.Response.builder()
      .id(1L)
      .email("test@gmail.com")
      .nickname("JPark")
      .profileImg("abc.jpg")
      .about("Hi")
      .carName("Sonata")
      .oilInfo("Gasoline")
      .build();
    
    MockMultipartFile profileImgFile = new MockMultipartFile(
      "profileImg"
      , "profileImg.jpg"
      , "image/jpeg"
      , new FileInputStream("C:\\Users\\Jinu_Park\\Desktop\\profileImg.jpg")
    );
    
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(member));
    BDDMockito.given(s3Service.uploadFile(Mockito.any(MultipartFile.class), Mockito.anyString())).willReturn("https://chamongbucket.s3.ap-northeast-2.amazonaws.com/images/default_image/member%28default%29.jpg");
    BDDMockito.given(mapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(memberResponseDto);
    
    //when
    MemberDto.Response response = memberService.updateMember(patchDto, profileImgFile, authorizedMemberDto);
    
    //then
    Assertions.assertEquals(1L, response.getId());
    Assertions.assertEquals("test@gmail.com", response.getEmail());
    Assertions.assertEquals("JPark", response.getNickname());
    Assertions.assertEquals("abc.jpg", response.getProfileImg());
    Assertions.assertEquals("Hi", response.getAbout());
    Assertions.assertEquals("Sonata", response.getCarName());
    Assertions.assertEquals("Gasoline", response.getOilInfo());
    
    BDDMockito.verify(memberRepository, VerificationModeFactory.times(1)).findByEmail(Mockito.anyString());
    BDDMockito.verify(s3Service, VerificationModeFactory.times(1)).uploadFile(Mockito.any(MultipartFile.class), Mockito.anyString());
    BDDMockito.verify(mapper, VerificationModeFactory.times(1)).memberToMemberResponseDto(Mockito.any(Member.class));
  }
  
  @Test
  public void Should_deleteMember_When_MemberExist(){
    //given
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto
      .builder()
      .id(1L)
      .email("test@gmail.com")
      .build();
    
    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(Member.builder().build()));
    BDDMockito.doNothing().when(memberRepository).deleteByEmail(Mockito.anyString());
    BDDMockito.doNothing().when(redisRepository).deleteBy(Mockito.anyString());
    
    //when
    memberService.deleteMember(authorizedMemberDto);
    
    //then
    BDDMockito.verify(memberRepository, VerificationModeFactory.times(1)).deleteByEmail(Mockito.anyString());
    BDDMockito.verify(redisRepository, VerificationModeFactory.times(1)).deleteBy(Mockito.anyString());
  }
  
  @Test
  public void Should_Logout_When_AuthorizationHeaderExist(){
    //given
    MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
    mockHttpServletRequest.addHeader("Authorization", "Bearer accessToken");
    
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto
      .builder()
      .id(1L)
      .email("test@gmail.com")
      .build();
    
    BDDMockito.doNothing().when(redisRepository).deleteBy(Mockito.anyString());
    BDDMockito.doNothing().when(redisRepository).setBlackList(Mockito.anyString());
    
    //when
    memberService.logout(mockHttpServletRequest, authorizedMemberDto);
    
    //then
    BDDMockito.verify(redisRepository, VerificationModeFactory.times(1)).deleteBy(Mockito.anyString());
    BDDMockito.verify(redisRepository, VerificationModeFactory.times(1)).setBlackList(Mockito.anyString());
  }
//  @Test
//  public void Should_Rollback_When_ThrowBusinessLogicException() throws IOException {
//    //given
//    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto
//      .builder()
//      .id(1L)
//      .email("test@gmail.com")
//      .build();
//
//    MemberDto.Patch patchDto = MemberDto.Patch
//      .builder()
//      .nickname("JPark")
//      .about("Hi")
//      .carName("Sonata")
//      .oilInfo("Gasoline")
//      .build();
//
//    MemberIntegrationTest member = MemberIntegrationTest.createMember(
//      MemberDto.Post
//        .builder()
//        .nickname("Spring")
//        .build()
//    );
//
//    MemberDto.Response memberResponseDto = MemberDto.Response.builder()
//      .id(1L)
//      .email("test@gmail.com")
//      .nickname("JPark")
//      .profileImg("abc.jpg")
//      .about("Hi")
//      .carName("Sonata")
//      .oilInfo("Gasoline")
//      .build();
//
//    MockMultipartFile profileImgFile = new MockMultipartFile(
//      "profileImg"
//      , "profileImg.jpg"
//      , "image/jpeg"
//      , new FileInputStream("C:\\Users\\Jinu_Park\\Desktop\\profileImg.jpg")
//    );
//
//    BDDMockito.given(memberRepository.findByEmail(Mockito.anyString())).willReturn(Optional.of(member));
//    BDDMockito.given(s3Service.uploadFile(Mockito.any(MultipartFile.class), Mockito.anyString())).willReturn("https://chamongbucket.s3.ap-northeast-2.amazonaws.com/images/default_image/member%28default%29.jpg");
//    BDDMockito.given(mapper.memberToMemberResponseDto(Mockito.any(MemberIntegrationTest.class))).willThrow(BusinessLogicException.class);
//
//    MemberDto.Response Response = memberService.updateMember(patchDto, profileImgFile, authorizedMemberDto);
//    BDDMockito.verify(memberRepository).findByEmail()
//  }


}
