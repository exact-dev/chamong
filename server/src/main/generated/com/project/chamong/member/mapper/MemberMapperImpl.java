package com.project.chamong.member.mapper;

import com.project.chamong.member.dto.MemberDto;
import com.project.chamong.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-21T16:14:15+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.18 (Azul Systems, Inc.)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member memberPostDtoToMember(MemberDto.Post postDto) {
        if ( postDto == null ) {
            return null;
        }

        Member member = new Member();

        member.setEmail( postDto.getEmail() );
        member.setPassword( postDto.getPassword() );
        member.setNickname( postDto.getNickname() );
        member.setProfileImg( postDto.getProfileImg() );
        member.setAbout( postDto.getAbout() );
        member.setCarName( postDto.getCarName() );
        member.setOilInfo( postDto.getOilInfo() );

        return member;
    }

    @Override
    public Member memberPatchDtoToMember(MemberDto.Patch patchDto) {
        if ( patchDto == null ) {
            return null;
        }

        Member member = new Member();

        member.setPassword( patchDto.getPassword() );
        member.setNickname( patchDto.getNickname() );
        member.setProfileImg( patchDto.getProfileImg() );
        member.setAbout( patchDto.getAbout() );
        member.setCarName( patchDto.getCarName() );
        member.setOilInfo( patchDto.getOilInfo() );

        return member;
    }

    @Override
    public MemberDto.Response memberToMemberResponseDto(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberDto.Response response = new MemberDto.Response();

        response.setId( member.getId() );
        response.setEmail( member.getEmail() );
        response.setNickname( member.getNickname() );
        response.setProfileImg( member.getProfileImg() );
        response.setAbout( member.getAbout() );
        response.setCarName( member.getCarName() );
        response.setOilInfo( member.getOilInfo() );

        return response;
    }

    @Override
    public Member memberToMember(Member sourceMember, Member targetMember) {
        if ( sourceMember == null ) {
            return targetMember;
        }

        targetMember.setPassword( sourceMember.getPassword() );
        targetMember.setNickname( sourceMember.getNickname() );
        targetMember.setProfileImg( sourceMember.getProfileImg() );
        targetMember.setAbout( sourceMember.getAbout() );
        targetMember.setCarName( sourceMember.getCarName() );
        targetMember.setOilInfo( sourceMember.getOilInfo() );
        targetMember.setValidation( sourceMember.getValidation() );
        if ( targetMember.getRoles() != null ) {
            List<String> list = sourceMember.getRoles();
            if ( list != null ) {
                targetMember.getRoles().clear();
                targetMember.getRoles().addAll( list );
            }
            else {
                targetMember.setRoles( null );
            }
        }
        else {
            List<String> list = sourceMember.getRoles();
            if ( list != null ) {
                targetMember.setRoles( new ArrayList<String>( list ) );
            }
        }

        return targetMember;
    }
}
