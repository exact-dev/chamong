package com.project.chamong.auth.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class TokenException extends AuthenticationException {
  AuthenticationExceptionCode authenticationExceptionCode;
  
  public TokenException(AuthenticationExceptionCode authenticationExceptionCode) {
    super(authenticationExceptionCode.getMessage());
    this.authenticationExceptionCode = authenticationExceptionCode;
  }
}
