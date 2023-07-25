package com.project.chamong.auth.service;

import com.project.chamong.auth.utils.CustomAuthorityUtils;
import com.project.chamong.member.entity.Member;
import com.project.chamong.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
  private final MemberService memberService;
  private final CustomAuthorityUtils customAuthorityUtils;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member findMember = memberService.findByEmail(username);
    
    return new UserDetailsImpl(findMember);
  }
  @Getter
  @AllArgsConstructor
  public final class UserDetailsImpl implements UserDetails {
    private Member member;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return customAuthorityUtils.createAuthority(member.getRoles());
    }
  
    @Override
    public String getPassword() {
      return member.getPassword();
    }
  
    @Override
    public String getUsername() {
      return member.getEmail();
    }
  
    @Override
    public boolean isAccountNonExpired() {
      return true;
    }
  
    @Override
    public boolean isAccountNonLocked() {
      return true;
    }
  
    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }
  
    @Override
    public boolean isEnabled() {
      return true;
    }

  }
}
