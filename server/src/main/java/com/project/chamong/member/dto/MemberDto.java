package com.project.chamong.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MemberDto {
  
  @Getter
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
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$",
      message = "password 길이는 최소 8자 이상 최대 20자 이하, 숫자 1자 이상, 대소문자 구분없이 영문자 1자 이상, 특수문자 1자 이상 입력 해주세요.")
    private String password;
    private String about;
    @NotBlank(message = "carName 는 공백일 수 없습니다.")
    private String carName;
    @NotBlank(message = "oilInfo 는 공백일 수 없습니다.")
    private String oilInfo;
    
  }
  
  @Setter
  @Getter
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Response{
    private Long id;
    private String email;
    private String nickname;
    private String profileImg;
    private String about;
    private String carName;
    private String oilInfo;
  }
  
  @Setter
  public static class MyPageResponse{
    MemberDto.Response memberInfo;
    
  }
}
