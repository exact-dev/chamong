package com.project.chamong.auth.config;

import com.project.chamong.auth.handler.MemberAccessDeniedHandler;
import com.project.chamong.auth.handler.MemberAuthenticationEntryPoint;
import com.project.chamong.auth.jwt.JwtProvider;
import com.project.chamong.auth.repository.TokenRedisRepository;
import com.project.chamong.member.repository.MemberRepository;
import com.project.chamong.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
  private final JwtProvider jwtProvider;
  private final TokenRedisRepository redisRepository;
  private final MemberRepository memberRepository;
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .httpBasic().disable()
      .formLogin().disable()
      .csrf().disable()
      .headers().frameOptions().sameOrigin()
      .and()
      .cors(Customizer.withDefaults())
      .apply(new JwtFilterConfiguration(jwtProvider, redisRepository, memberRepository))
      .and()
      .exceptionHandling()
      .accessDeniedHandler(new MemberAccessDeniedHandler())
      .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
      .and()
      .authorizeHttpRequests(authorize -> { authorize
        .antMatchers(HttpMethod.PATCH,"/members").hasRole("USER")
        .antMatchers(HttpMethod.DELETE,"/members").hasRole("USER")
        .antMatchers(HttpMethod.GET,"/members/mypage").hasRole("USER")
        .antMatchers(HttpMethod.GET,"/members/logout").hasRole("USER")
        .antMatchers(HttpMethod.POST,"/articles").hasRole("USER")
        .antMatchers(HttpMethod.GET,"/articles/**").hasRole("USER")
        .antMatchers(HttpMethod.GET,"/articles").hasRole("USER")
        .antMatchers(HttpMethod.PATCH,"/articles/**").hasRole("USER")
        .antMatchers(HttpMethod.DELETE,"/articles/**").hasRole("USER")
        .antMatchers(HttpMethod.POST,"/Article-likes/**").hasRole("USER")
        .antMatchers(HttpMethod.DELETE,"/Article-likes/**").hasRole("USER")
        .antMatchers(HttpMethod.POST,"/comments/**").hasRole("USER")
        .antMatchers(HttpMethod.PATCH,"/comments/**").hasRole("USER")
        .antMatchers(HttpMethod.DELETE,"/comments/**").hasRole("USER")
        .antMatchers(HttpMethod.POST,"/members").permitAll()
        .antMatchers(HttpMethod.POST,"/members/login").permitAll();
      });
    
    return http.build();
  }
  
  @Bean
  CorsConfigurationSource corsConfig(){
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Refresh"));
    corsConfiguration.setExposedHeaders(Arrays.asList("Authorization", "Refresh"));
    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PATCH"));
    corsConfiguration.setMaxAge(3600l);
  
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    
    return source;
  }
  @Bean
  PasswordEncoder passwordEncoder(){
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
