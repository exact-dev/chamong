package com.project.chamong.auth.jwt;

import com.project.chamong.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
  private Key key;
  
  @Value("${jwt.access-token-expiration-millisecond}")
  private int accessTokenExpirationMillisecond;
  
  @Value("${jwt.refresh-token-expiration-millisecond}")
  private int refreshTokenExpirationMillisecond;
  
  public JwtProvider(@Value("${jwt.key}") String key) {
    String encodedKey = Encoders.BASE64.encode(key.getBytes());
    byte[] keyBytes = Decoders.BASE64.decode(encodedKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }
  
  public String generateAccessToken(Member member){
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MILLISECOND, accessTokenExpirationMillisecond);
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", member.getRoles());
    
    return Jwts.builder()
      .setClaims(claims)
      .setSubject(member.getEmail())
      .setIssuedAt(Calendar.getInstance().getTime())
      .setExpiration(calendar.getTime())
      .signWith(key)
      .compact();
  }
  
  public String generateRefreshToken(Member member){
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MILLISECOND, accessTokenExpirationMillisecond);
    
    return Jwts.builder()
      .setSubject(member.getEmail())
      .setIssuedAt(Calendar.getInstance().getTime())
      .setExpiration(calendar.getTime())
      .signWith(key)
      .compact();
  }
  
  public Claims parseClaims(String jws){
    return Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(jws)
      .getBody();
  }
}
