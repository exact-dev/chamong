package com.project.chamong.member.mapper;

import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
  
  Member memberPostDtoToMember(MemberDto.Post postDto);
  
  Member memberPatchDtoToMember(MemberDto.Patch patchDto);
  
  MemberDto.Response memberToMemberResponseDto(Member member);
}
