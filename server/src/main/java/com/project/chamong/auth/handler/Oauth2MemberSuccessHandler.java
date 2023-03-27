package com.project.chamong.auth.handler;

import com.project.chamong.auth.jwt.JwtProvider;
import com.project.chamong.auth.repository.TokenRedisRepository;
import com.project.chamong.auth.utils.CustomAuthorityUtils;
import com.project.chamong.auth.utils.Responder;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Oauth2MemberSuccessHandler implements AuthenticationSuccessHandler {
  
  private final JwtProvider jwtProvider;
  private final MemberRepository memberRepository;
  private final TokenRedisRepository redisRepository;
  
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    System.out.println("oauth Success Handler 호출됨");
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
  
    Member member = findMemberOrCreate(oAuth2User);
  
    String accessToken = jwtProvider.generateAccessToken(member);
    String refreshToken = jwtProvider.generateRefreshToken(member);
  
    redisRepository.save(member.getEmail(), refreshToken);
  
    response.setHeader("Authorization", "Bearer " + accessToken);
    response.setHeader("Refresh", refreshToken);
  
    Responder.sendSuccessResponse(response);
  }
  private Member findMemberOrCreate(OAuth2User oAuth2User){
    Map<String, Object> attributes = oAuth2User.getAttributes();
    String email = String.valueOf(attributes.get("email"));
    
    try {
      return memberRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    } catch (BusinessLogicException exception){
      MemberDto.Post postDto = MemberDto.Post.builder()
        .email(email)
        .nickname(String.valueOf(attributes.get("name")))
        .build();
  
      Member member = Member.createMember(postDto);
      member.setRoles(CustomAuthorityUtils.crateRoles(member.getEmail()));
  
      return memberRepository.save(member);
    }
  }
  
}
