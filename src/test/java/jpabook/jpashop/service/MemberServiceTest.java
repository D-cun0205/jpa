package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원 가입")
    void join() throws Exception {
        //given
        Member member = new Member();
        member.setName("sanghun");

        //when
        Long memberId = memberRepository.save(member);

        //then
        assertEquals(member, memberRepository.findOne(memberId));

    }

    @Test
    @DisplayName("중복 회원 예외")
    void duplicateMember() {
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            Member member1 = new Member();
            member1.setName("회원1");

            Member member2 = new Member();
            member2.setName("회원1");

            memberService.save(member1);
            memberService.save(member2); //중복 회원 발생
        });

        assertTrue(thrown.getMessage().contains("Already is joined by member"));
    }
}