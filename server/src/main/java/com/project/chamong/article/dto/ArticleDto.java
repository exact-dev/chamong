package com.project.chamong.article.dto;

import com.project.chamong.member.dto.MemberDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleDto {
    @Getter
    @Setter
    public static class Response {
        private Long id;
        @NotBlank(message = "제목을 입력해주세요")
        @Length(max = 100, message = "Title은 100자 이하여야 합니다.")
        private String title;
        @NotBlank(message = "내용을 입력해주세요")
        @Length(max = 1000, message = "content는 1000자 이하여야 합니다.")
        private String content;
        private String nickName;
        private String profileImg;
        private String oilInfo;
        private String articleImg;
        private String createdAt;
        private String updatedAt;
        private boolean like;
        private MemberDto.Response member;
        private int viewCnt;
        private int likeCnt;
        private int commentCnt;

    }

    @Getter
    @Setter
    public static class Post {
        @NotBlank(message = "제목을 입력해주세요")
        @Length(max = 100, message = "Title은 100자 이하여야 합니다.")
        private String title;
        @NotBlank(message = "내용을 입력해주세요")
        @Length(max = 1000, message = "content는 1000자 이하여야 합니다.")
        private String content;
        private String articleImg;
        private Long memberId;
        private MemberDto.Response nickname;
    }

    @Getter
    @Setter
    public static class Patch {
        @NotBlank(message = "제목을 입력해주세요")
        @Length(max = 100, message = "Title은 100자 이하여야 합니다.")
        private String title;
        @NotBlank(message = "내용을 입력해주세요")
        @Length(max = 1000, message = "content는 1000자 이하여야 합니다.")
        private String content;
        private String articleImg;
    }
}
