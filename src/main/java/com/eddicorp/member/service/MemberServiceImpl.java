package com.eddicorp.member.service;

import com.eddicorp.member.Member;
import com.eddicorp.member.repository.MemberRepository;
import com.eddicorp.member.repository.MemberRepositoryImpl;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository = MemberRepositoryImpl.getInstance();

    @Override
    public void signUp(String username, String password) {
        final Member member = new Member(0L, username, password);
        memberRepository.signUp(member);
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
