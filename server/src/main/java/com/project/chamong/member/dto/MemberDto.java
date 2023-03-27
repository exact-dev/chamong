package com.project.chamong.member.dto;

import com.project.chamong.article.dto.ArticleDto;
import com.project.chamong.place.dto.MyPlaceDto;
import com.project.chamong.place.dto.VisitedPlaceDto;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

public class MemberDto {
  
  @Getter
  @Builder
  public static class Post{
    @NotBlank(message = "email 은 공백일 수 없습니다.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String email;
    @NotBlank(message = "nickname 는 공백일 수 없습니다.")
    @Length(max = 20, message = "nickname 길이는 최대 20자 이하로 입력해 주세요.")
    private String nickname;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$",
      message = "password 길이는 최소 8자 이상 최대 20자 이하, 숫자 1자 이상, 대소문자 구분없이 영문자 1자 이상, 특수문자 1자 이상 입력 해주세요.")
    private String password;
  }
  @Getter
  public static class Patch{
    @NotBlank(message = "nickname 는 공백일 수 없습니다.")
    @Length(max = 20, message = "nickname 길이는 최대 20자 이하로 입력해 주세요.")
    private String nickname;
    private String about;
    @NotBlank(message = "carName 는 공백일 수 없습니다.")
    private String carName;
    @NotBlank(message = "oilInfo 는 공백일 수 없습니다.")
    private String oilInfo;
    
  }
  
  @Setter
  @Getter
  public static class Response{
    private Long id;
    private String email;
    private String nickname;
    private String profileImg;
    private String about;
    private String carName;
    private String oilInfo;
  }
  @Getter
  @Setter
  @Builder
  public static class MyPageResponse{
    MemberDto.Response memberInfo;
    List<MyPlaceDto.Response> myPlaceInfos;
    List<VisitedPlaceDto.Response> visitedPlaceInfos;
    List<ArticleDto.Response> writtenArticleInfos;
    List<ArticleDto.Response> commentedArticleInfos;
    List<ArticleDto.Response> likedArticleInfos;
    
  }
}
