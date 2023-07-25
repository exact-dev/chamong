package com.project.chamong.auth.handler;

import com.project.chamong.auth.exception.AuthenticationExceptionCode;
import com.project.chamong.auth.exception.TokenException;
import com.project.chamong.auth.utils.Responder;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {
  
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    Exception exception = (Exception) request.getAttribute("exception");
    
    if(exception instanceof TokenException){
      AuthenticationExceptionCode exceptionCode = ((TokenException) exception).getAuthenticationExceptionCode();
      Responder.sendErrorResponse(
        response, HttpStatus.valueOf(exceptionCode.getStatus()),exceptionCode.getMessage());
    } else if(exception instanceof SignatureException){
      Responder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, exception.getMessage());
    } else if (authException instanceof InsufficientAuthenticationException){
      Responder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "비회원 입니다. 접근할 권한이 없습니다.");
    }
  }
}
