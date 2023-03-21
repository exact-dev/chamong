package com.project.chamong.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthorizedMemberDto {
  private Long id;
  private String email;
}
