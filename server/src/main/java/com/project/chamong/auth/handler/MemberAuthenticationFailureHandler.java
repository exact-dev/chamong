package com.project.chamong.auth.handler;

import com.project.chamong.auth.utils.Responder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {
  
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    Responder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "자격 증명에 실패했습니다. ID 또는 Password 를 확인해 주세요.");
  }
}
