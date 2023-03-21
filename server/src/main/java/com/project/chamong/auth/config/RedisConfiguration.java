package com.project.chamong.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
  
  @Value("${spring.redis.host}")
  private String host;
  @Value("${spring.redis.port}")
  private String port;
//  @Value("${spring.redis.password}")
//  private String password;
  
  @Bean
  RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
    redisConfiguration.setHostName(host);
    redisConfiguration.setPort(Integer.parseInt(port));
//    redisConfiguration.setPassword(password);
    return new LettuceConnectionFactory(redisConfiguration);
  }
  
  @Bean
  RedisTemplate<String, String> redisTemplate() {
    StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory());
    return stringRedisTemplate;
  }
}
