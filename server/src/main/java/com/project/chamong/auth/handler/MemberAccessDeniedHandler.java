package com.project.chamong.auth.handler;

import com.project.chamong.auth.utils.Responder;
import com.project.chamong.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberAccessDeniedHandler implements AccessDeniedHandler {
  
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    Responder.sendErrorResponse(response, HttpStatus.FORBIDDEN,  "접근할 권한이 없습니다.");
  }
}
