package com.project.chamong.auth.utils;

import com.google.gson.Gson;
import com.project.chamong.dto.ResponseDto;
import com.project.chamong.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Responder {
  
  public static void sendSuccessResponse(HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.OK.value());
    response.getWriter().write(
      gson.toJson(
        ResponseDto.success(null, "정상적으로 로그인 되었습니다."), ResponseDto.class
      )
    );
  }
  
  public static void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
    Gson gson = new Gson();
    ErrorResponse errorResponse;
    if(message != null){
      errorResponse = ErrorResponse.of(status, message);
    }else {
      errorResponse = ErrorResponse.of(status);
    }
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(status.value());
    response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
  }
}
