package com.project.chamong.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.chamong.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

  private T data;
  private String message;
  private ExceptionCode exceptionCode;

  public static <T> ResponseDto<T> success(@Nullable T data, @Nullable String message){
    return new ResponseDto<>(data, message,null);
  }
}
