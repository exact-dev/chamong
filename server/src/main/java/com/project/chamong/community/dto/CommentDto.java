package com.project.chamong.community.dto;

import com.project.chamong.community.entity.Comment;
import com.project.chamong.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private MemberDto nickName;
    // profile img
    private MemberDto img;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;


    // 프로필 이미지, 닉네임, 댓글 작성 시간, 내용
    public static class Post {

        private Long id;
        private String content;
        private MemberDto nickName;
        // profile img
        private MemberDto img;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
    }

    public static class Response {
        private Long id;
        private String content;
        private MemberDto nickName;
        private MemberDto img;
        private LocalDateTime createAt;
    }

}
