package com.project.chamong.auth.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class CustomAuthorityUtils {
  private static String adminEmail;
  private static final List<String> ADMIN_ROLES = List.of("ROLE_ADMIN", "ROLE_USER");
  private static final List<String> USER_ROLES = List.of("ROLE_USER");
  @Value("${admin.email}")
  public void setAdminEmail(String adminEmail){
    this.adminEmail = adminEmail;
  }
  
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
