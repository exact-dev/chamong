package com.project.chamong.auth.filter;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import com.project.chamong.auth.exception.AuthenticationExceptionCode;
import com.project.chamong.auth.exception.TokenException;
import com.project.chamong.auth.jwt.JwtProvider;
import com.project.chamong.auth.repository.TokenRedisRepository;
import com.project.chamong.auth.utils.CustomAuthorityUtils;
import com.project.chamong.exception.BusinessLogicException;
import com.project.chamong.exception.ExceptionCode;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
  private final JwtProvider jwtProvider;
  private final TokenRedisRepository redisRepository;
  private final MemberRepository memberRepository;
  private final String HEADER_PREFIX = "Bearer ";
  
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      Claims claims = verifyJws(request, response);
      setAuthenticationToContext(claims);
    } catch (ExpiredJwtException exception) {
      try {
        if(request.getHeader("Refresh") != null) {
          throw new TokenException(AuthenticationExceptionCode.EXPIRED_REFRESH_TOKEN);
        }
        throw new TokenException(AuthenticationExceptionCode.EXPIRED_ACCESS_TOKEN);
      } catch (TokenException tokenException){
        request.setAttribute("exception", tokenException);
      }
    }catch (SignatureException exception) {
      request.setAttribute("exception", exception);
    }catch (TokenException exception) {
      request.setAttribute("exception", exception);
    }
  
    filterChain.doFilter(request, response);
  }
  
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String accessToken = request.getHeader("Authorization");
    String refreshToken = request.getHeader("Refresh");
    
    return !(accessToken != null && accessToken.startsWith(HEADER_PREFIX)) && !(refreshToken != null);
  }
  
  public Claims verifyJws(HttpServletRequest request, HttpServletResponse response) {
    String accessToken = request.getHeader("Authorization");
    String refreshToken = request.getHeader("Refresh");
    
    if(accessToken != null){
      accessToken = accessToken.substring(HEADER_PREFIX.length());
  
      if (redisRepository.findBy(accessToken) != null) {
        throw new TokenException(AuthenticationExceptionCode.LOGGED_OUT_MEMBER);
      }
  
      return jwtProvider.parseClaims(accessToken);
    }
    
    Claims claims = jwtProvider.parseClaims(refreshToken);
    String findRefreshToken = redisRepository.findBy(claims.getSubject());
    
    if(!refreshToken.equals(findRefreshToken)){
      throw new TokenException(AuthenticationExceptionCode.MISMATCHED_TOKEN);
    }
    
    Member member = memberRepository.findByEmail(claims.getSubject()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    String newAccessToken = jwtProvider.generateAccessToken(member);
    response.setHeader("Authorization", HEADER_PREFIX + newAccessToken);
    return jwtProvider.parseClaims(newAccessToken);
    
  }
  
  public void setAuthenticationToContext(Claims claims){
    Member member = memberRepository.findByEmail(claims.getSubject()).orElseThrow();
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto.builder().id(member.getId()).email(member.getEmail()).build();
    List<GrantedAuthority> authorities = CustomAuthorityUtils.createAuthority(member.getRoles());
    UsernamePasswordAuthenticationToken authenticatedToken =
      UsernamePasswordAuthenticationToken.authenticated(authorizedMemberDto, null, authorities);
  
    SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
  }
}
