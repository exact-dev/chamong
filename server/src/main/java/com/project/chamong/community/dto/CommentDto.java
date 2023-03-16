package com.project.chamong.community.dto;

import com.project.chamong.community.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    // 프로필 이미지, 닉네임, 댓글 작성 시간, 내용
    private Long communityId;
    private String content;
    // private UserDto nickName;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    // private UserDto profiile_img;

    public Comment toEntity(){
        return Comment.builder()
                .content(content)
                .build();
    }
}
