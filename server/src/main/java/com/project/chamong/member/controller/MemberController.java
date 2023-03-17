package com.project.chamong.member.controller;

import com.project.chamong.auth.dto.AuthorizedMember;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.mapper.MemberMapper;
import com.project.chamong.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
  public ResponseEntity<?> getMyPage(@AuthenticationPrincipal AuthorizedMember authorizedMember){
    return new ResponseEntity<>(HttpStatus.OK);
  }
  
  @PatchMapping
  public ResponseEntity<?> patchMember(@Valid @RequestBody MemberDto.Patch patchDto,
                                       @AuthenticationPrincipal AuthorizedMember authorizedMember){
  
    Member savedMember = memberService.updateMember(mapper.memberPatchDtoToMember(patchDto), authorizedMember.getEmail());
    MemberDto.Response response = mapper.memberToMemberResponseDto(savedMember);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  
  @DeleteMapping
  public ResponseEntity<?> deleteMember(@AuthenticationPrincipal Member member){
    memberService.deleteMember(member.getEmail());
    return new ResponseEntity<>(HttpStatus.OK);
  }
  
  
}
