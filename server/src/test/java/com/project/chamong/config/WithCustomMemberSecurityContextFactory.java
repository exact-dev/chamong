package com.project.chamong.config;

import com.project.chamong.auth.dto.AuthorizedMemberDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMemberSecurityContextFactory implements WithSecurityContextFactory<WithAuthorizedMemberDto> {
  
  @Override
  public SecurityContext createSecurityContext(WithAuthorizedMemberDto annotation) {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    
    AuthorizedMemberDto authorizedMemberDto = AuthorizedMemberDto.builder()
      .id(annotation.id())
      .email(annotation.email())
      .build();
    
    UsernamePasswordAuthenticationToken authenticatedToken = UsernamePasswordAuthenticationToken.authenticated(
      authorizedMemberDto, null, AuthorityUtils.createAuthorityList(annotation.roles()));
    
    securityContext.setAuthentication(authenticatedToken);
    
    return securityContext;
  }
}
