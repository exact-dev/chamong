package com.project.chamong.member.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.auth.repository.TokenRedisRepository;
import com.project.chamong.auth.utils.CustomAuthorityUtils;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.mapper.MemberMapper;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.s3.service.S3Service;
import com.project.chamong.utils.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenRedisRepository redisRepository;
  private final MemberMapper mapper;
  private final S3Service s3Service;
  private String dirName = "member_image/";
  
  public MemberDto.Response saveMember(MemberDto.Post postDto){
    verifyExistEmail(postDto.getEmail());
  
    postDto.setPassword(passwordEncoder.encode(postDto.getPassword()));
    postDto.setRoles(CustomAuthorityUtils.crateRoles(postDto.getEmail()));
    postDto.setProfileImg(s3Service.getDefaultProfileImg());
    
    Member member = Member.createMember(postDto);
    
    memberRepository.save(member);
    
    return mapper.memberToMemberResponseDto(member);
  }
  
  public MemberDto.MyPageResponse findMyPage(AuthorizedMemberDto authorizedMemberDto){
  
    Member findMember = findByEmail(authorizedMemberDto.getEmail());
  
    return mapper.memberToMemberMypageResponse(findMember);
  }
  @Transactional
  public MemberDto.Response updateMember(MemberDto.Patch patchDto, MultipartFile profileImg, AuthorizedMemberDto authorizedMemberDto) {
    Member findMember = findByEmail(authorizedMemberDto.getEmail());
    
    if(!profileImg.isEmpty()){
      patchDto.setProfileImg(s3Service.uploadFile(profileImg, dirName));
    }
    
    findMember.updateMember(patchDto);
  
    return mapper.memberToMemberResponseDto(findMember);
    
  }
  @Transactional
  public void deleteMember(String email) {
    memberRepository.deleteByEmail(email);
    redisRepository.deleteBy(email);
  }
  
  public Member findByEmail(String email){
    Optional<Member> optionalMember = memberRepository.findByEmail(email);
    return optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
  }
  
  public void verifyExistEmail(String email){
    Optional<Member> optionalMember = memberRepository.findByEmail(email);
    if (optionalMember.isPresent()){
      throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }
  }
  
  public void logout(HttpServletRequest request, AuthorizedMemberDto authorizedMemberDto){
    String accessToken = request.getHeader("Authorization").substring(7);
    // 저장된 Refresh 토큰 삭제
    redisRepository.deleteBy(authorizedMemberDto.getEmail());
    
    // Access 토큰을 저장하여 블랙리스트로 등록하여 이후 해당 토큰으로 요청시 거절함
    redisRepository.setBlackList(accessToken);
  }
  
  public MemberDto.Response reissueAccessToken(AuthorizedMemberDto authorizedMemberDto){
    Member findMember = findByEmail(authorizedMemberDto.getEmail());
    return mapper.memberToMemberResponseDto(findMember);
  }
}
