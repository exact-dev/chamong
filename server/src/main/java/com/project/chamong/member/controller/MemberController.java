package com.project.chamong.member.controller;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.mapper.MemberMapper;
import com.project.chamong.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
  private final MemberService memberService;
  
  @PostMapping
  public ResponseEntity<?> postMember(@Valid @RequestBody MemberDto.Post postDto) {
    MemberDto.Response response = memberService.saveMember(postDto);
  
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
  
  @GetMapping("/mypage")
  public ResponseEntity<?> getMyPage(@AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
    MemberDto.MyPageResponse response = memberService.findMyPage(authorizedMemberDto);
  
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  
  @PatchMapping
  public ResponseEntity<?> patchMember(@Valid @RequestPart("memberUpdate") MemberDto.Patch patchDto,
                                       @RequestPart MultipartFile profileImg,
                                       @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto) throws IOException {
  
    MemberDto.Response response = memberService.updateMember(patchDto, profileImg, authorizedMemberDto);
    
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  
  @DeleteMapping
  public ResponseEntity<?> deleteMember(@AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
    memberService.deleteMember(authorizedMemberDto.getEmail());
    String message = "정상적으로 회원 탈퇴 되었습니다.";
    
    return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
  }
  
  @GetMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request,
                                  @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
    memberService.logout(request, authorizedMemberDto);
    String message = "정상적으로 로그아웃 되었습니다.";
    return new ResponseEntity<>(message, HttpStatus.OK);
  }
  //실제 AccessToken 재발급 로직은 JwtVerificationFilter 에서 처리함
  @GetMapping("/token")
  public ResponseEntity<?> reissueAccessToken(@AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
    MemberDto.Response response = memberService.reissueAccessToken(authorizedMemberDto);
    
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
