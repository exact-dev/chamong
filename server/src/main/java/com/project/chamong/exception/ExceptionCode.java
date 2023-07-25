package com.project.chamong.exception;

import lombok.Getter;


public enum ExceptionCode {
  MEMBER_NOT_FOUND(404, "회원이 존재하지 않습니다."),
  MEMBER_EXISTS(409, "이미 존재하는 회원 입니다."),
  QUESTION_NOT_FOUND(404, "질문 글이 존재하지 않습니다."),
  ANSWER_NOT_FOUND(404, "답변 글이 존재하지 않습니다."),
  ANSWER_SCORE_EXISTS(409, "답변에 이미 좋아요를 누르셨습니다."),
  ANSWER_SCORE_NOT_FOUND(404, "답변에 좋아요를 누르지 않으셨습니다."),
  QUESTION_SCORE_EXISTS(409, "질문에 이미 좋아요를 누르셨습니다."),
  QUESTION_SCORE_NOT_FOUND(404, "질문에 좋아요를 누르지 않으셨습니다."),
  QUESTION_UPDATE_NO_PERMISSION(404, "질문 작성자만 질문을 수정할 수 있습니다."),
  QUESTION_DELETE_NO_PERMISSION(404, "질문 작성자만 질문을 삭제할 수 있습니다."),
  ANSWER_UPDATE_NO_PERMISSION(404, "답변 작성자만 답변을 수정할 수 있습니다."),
  ANSWER_DELETE_NO_PERMISSION(404, "답변 작성자만 답변을 삭제할 수 있습니다."),
  CONTENT_NOT_FOUND(404, "해당 캠핑장 게시물이 존재하지 않습니다."),
  REVIEW_NOT_FOUND(404, "해당 리뷰가 존재하지 않습니다."),
  VISITED_PLACE_EXISTS(409, "방문한 장소에 이미 추가되었습니다."),
  VISITED_PLACE_NOT_FOUND(404, "방문한 장소가 존재하지 않습니다."),
  VISITED_PLACE_DELETE_NO_PERMISSION(404, "방문한 장소를 삭제할 권한이 없습니다."),
  MY_PLACE_NOT_FOUND(404, "나만의 차박지 장소가 존재하지 않습니다."),
  MY_PLACE_UPDATE_OR_DELETE_NO_PERMISSION(404, "나만의 차박지를 수정, 삭제할 권한이 없습니다. 등록자 본인만 수정, 삭제가 가능 합니다."),
  BOOKMARK_NOT_FOUND(404, "해당 북마크가 존재하지 않습니다."),
  ARTICLE_NOT_FOUND(404,"해당게시물이 존재하지 않습니다." );

  @Getter
  private int status;

  @Getter
  private String message;

  ExceptionCode(int code, String message) {
    this.status = code;
    this.message = message;
  }
}
