package com.example.springstudy.repository;

import com.example.springstudy.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("memberV0", 10000);
        memberRepository.save(member);

        // findById
        Member findMember = memberRepository.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(member);

        // update
        memberRepository.update(member.getMemberId(), 20000);
        Member updatedMember = memberRepository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        // delete
        memberRepository.delete(member.getMemberId());
        assertThatThrownBy(() -> memberRepository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}