package com.project.chamong.article.dto;

import com.project.chamong.article.entity.Comment;
import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
// MemberDto, Member, String??
@Getter
@Setter
public class CommentDto {
    private Long id;
    private String content;
    private Long articleId;
    private Long memberId;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Member profileImg;
    // private MemberDto profileImg;
    private MemberDto nickName;

    // 프로필 이미지, 닉네임, 댓글 작성 시간, 내용
    @Getter
    @Setter
    public static class Post {
        private String content;
        private String nickName;
        private Long articleId;
        private Long memberId;
    }
    @Getter
    @Setter
    public static class Response {
        private Long id;
        private String content;
        private Long articleId;
        private Long memberId;
        private String nickName;
        private String profileImg;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }

    @Getter
    @Setter
    public static class Patch{
        private String content;
        public void update(Comment comment){
            comment.setContent(content);
        }
    }

}
