package com.project.chamong.auth.service;

import com.project.chamong.auth.utils.CustomAuthorityUtils;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
//@Service
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
  private final PasswordEncoder passwordEncoder;
  private final MemberService memberService;
  
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    return null;
  }
  
  //  @Override
//  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//    OAuth2UserService userService = new DefaultOAuth2UserService();
//    OAuth2User oAuth2User = userService.loadUser(userRequest);
//    findMemberOrCreate(oAuth2User);
//
//  }
//
//  private void findMemberOrCreate(OAuth2User oAuth2User){
//    Map<String, Object> attributes = oAuth2User.getAttributes();
//    String email = String.valueOf(attributes.get("email"));
//    Member member;
//
//    try {
//      memberService.findByEmail(email);
//    }catch (BusinessLogicException exception){
//      MemberDto.Post postDto = MemberDto.Post.builder()
//        .email(email)
//        .nickname(String.valueOf(attributes.get("name")))
//        .isSocialMember(true)
//        .build();
//
//      member = Member.createMember(postDto);
//      memberService.saveMember(member);
//    }
//  }
}
