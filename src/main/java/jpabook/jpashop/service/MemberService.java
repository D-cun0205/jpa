package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        //멀티스레딩으로 인한 중복 인서트가 가능할 수 있다, Member name 유니크 제약 조건 사용 권의
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("Already is joined by member");
        }
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional
    public void update(Long id, String name) {
        Member findMember = memberRepository.findOne(id);
        findMember.setName(name);
    }
}