package com.project.chamong.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chamong.auth.dto.LoginDto;
import com.project.chamong.auth.jwt.JwtProvider;
import com.project.chamong.auth.repository.TokenRedisRepository;
import com.project.chamong.auth.service.UserDetailsServiceImpl;
import com.project.chamong.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final JwtProvider jwtProvider;
  private final TokenRedisRepository tokenRedisRepository;
  
  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    ObjectMapper objectMapper = new ObjectMapper();
    LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
    UsernamePasswordAuthenticationToken authRequest =
      UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getEmail(), loginDto.getPassword());
    return getAuthenticationManager().authenticate(authRequest);
  }
  
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    Member member = ((UserDetailsServiceImpl.UserDetailsImpl) authResult.getPrincipal()).getMember();
    String accessToken = jwtProvider.generateAccessToken(member);
    String refreshToken = jwtProvider.generateRefreshToken(member);
  
    tokenRedisRepository.save(member.getEmail(), refreshToken);
    
    response.setHeader("Authorization", "Bearer " + accessToken);
    response.setHeader("Refresh", refreshToken);
    
    getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
  }
}
