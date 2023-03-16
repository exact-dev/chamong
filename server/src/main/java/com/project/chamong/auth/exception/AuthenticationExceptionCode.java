package com.project.chamong.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthenticationExceptionCode {
  LOGGED_OUT_MEMBER(401, "로그아웃 또는 회원 탈퇴한 토큰 입니다."),
  EXPIRED_ACCESS_TOKEN(401, "Access 토큰이 만료됐습니다. Refresh 토큰이 필요합니다."),
  EXPIRED_REFRESH_TOKEN(401, "Refresh 토큰이 만료됐습니다. 다시 로그인 하시기 바랍니다."),
  MISMATCHED_TOKEN(401, "Refresh 토큰이 만료됐습니다. 다시 로그인 하시기 바랍니다.");
  private int status;
  private String message;
}
