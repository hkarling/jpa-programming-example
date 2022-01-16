package io.hkarling.service;

import io.hkarling.domain.Member;
import io.hkarling.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    public void join() {
        // given
        Member member = new Member();
        member.setName("NAME_A");

        // when
        Long savedId = memberService.join(member);

        // then
        em.flush();
        assertEquals(member, memberRepository.findById(savedId));
    }

    @Test
    public void validateDuplicatedMember() {
        // given
        Member member1 = new Member();
        member1.setName("MEMBER_A");

        Member member2 = new Member();
        member2.setName("MEMBER_A");

        // when
        memberService.join(member1);
//        try {
//            memberService.join(member2);
//        } catch (IllegalStateException e) {
//            return;
//        }

        // then
        //fail("EXCEPTION OCCURS");
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));

    }

}