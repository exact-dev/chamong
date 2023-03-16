package com.project.chamong.auth.repository;

import com.project.chamong.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class TokenRedisRepository {
  
  private final JwtProvider jwtProvider;
  private final StringRedisTemplate redisTemplate;
  
  public void save(String email, String refreshToken){
    redisTemplate.opsForValue().set(
      email,
      refreshToken,
      jwtProvider.parseClaims(refreshToken).getExpiration().getTime() - System.currentTimeMillis(),
      TimeUnit.MILLISECONDS
    );
  }
  
  public String findBy(String key){
    return redisTemplate.opsForValue().get(key);
  }
  
  public void deleteBy(String key){
    redisTemplate.delete(key);
  }
  
  public void setBlackList(String accessToken){
    redisTemplate.opsForValue().set(
      accessToken,
      "logout",
      jwtProvider.parseClaims(accessToken).getExpiration().getTime() - System.currentTimeMillis(),
      TimeUnit.MILLISECONDS
    );
  }
}
