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

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
  private final MemberMapper mapper;
  private final MemberService memberService;
  @PostMapping
  public ResponseEntity<?> postMember(@Valid @RequestBody MemberDto.Post postDto){
    Member member = mapper.memberPostDtoToMember(postDto);
    Member savedMember = memberService.saveMember(member);
    MemberDto.Response response = mapper.memberToMemberResponseDto(savedMember);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  
  @GetMapping("/mypage")
  public ResponseEntity<?> getMyPage(@AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
    Member findMember = memberService.findMyPage(authorizedMemberDto);
    MemberDto.MyPageResponse response = mapper.memberToMemberMypageResponse(findMember);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  
  @PatchMapping
  public ResponseEntity<?> patchMember(@Valid @RequestPart("memberUpdate") MemberDto.Patch patchDto,
                                       @RequestPart MultipartFile profileImg,
                                       @AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto) throws IOException {
    
    Member savedMember = memberService.updateMember(mapper.memberPatchDtoToMember(patchDto), authorizedMemberDto.getEmail());
    MemberDto.Response response = mapper.memberToMemberResponseDto(savedMember);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  
  @DeleteMapping
  public ResponseEntity<?> deleteMember(@AuthenticationPrincipal AuthorizedMemberDto authorizedMemberDto){
    memberService.deleteMember(authorizedMemberDto.getEmail());
    return new ResponseEntity<>(HttpStatus.OK);
  }
  
  
}
