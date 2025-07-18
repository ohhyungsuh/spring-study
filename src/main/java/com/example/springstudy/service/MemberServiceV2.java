package com.example.springstudy.service;

import com.example.springstudy.domain.Member;
import com.example.springstudy.repository.MemberRepositoryV1;
import com.example.springstudy.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            // 트랜잭션 시작
            connection.setAutoCommit(false);

            // 로직
            bizLogic(connection, money, fromId, toId);

            // 성공시 커밋
            connection.commit();

        } catch (Exception e) {
            // 실패시 커밋
            connection.rollback();
            throw new IllegalStateException(e);
        } finally {
            if(connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (Exception e) {
                    log.error("error", e);
                }
            }
        }
    }

    private void bizLogic(Connection connection, int money, String fromId, String toId) throws SQLException {
        Member fromMember = memberRepository.findById(connection, fromId);
        Member toMember = memberRepository.findById(connection, toId);

        memberRepository.update(connection, fromId, fromMember.getMoney() - money);
        validateMember(toMember);
        memberRepository.update(connection, toId, toMember.getMoney() + money);
    }

    private void validateMember(Member toMember) {
        if(toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
