package com.project.chamong.auth.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomAuthorityUtils {
  @Value("${admin.email}")
  private static String adminEmail;
  private static final List<String> ADMIN_ROLES = List.of("ROLE_ADMIN", "ROLE_USER");
  private static final List<String> USER_ROLES = List.of("ROLE_ADMIN", "ROLE_USER");
  
  public static List<GrantedAuthority> createAuthority(List<String> roles){
    return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
  }
  
  public static List<String> crateRoles(String email){
    if(adminEmail.equals(email)){
      return ADMIN_ROLES;
    }
    return USER_ROLES;
  }
}
