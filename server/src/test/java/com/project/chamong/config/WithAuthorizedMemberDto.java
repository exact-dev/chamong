package com.project.chamong.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMemberSecurityContextFactory.class)
public @interface WithAuthorizedMemberDto {
  long id();
  String email();
  String roles();
}
