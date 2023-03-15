package com.eddicorp.member.service;

import com.eddicorp.member.Member;
import com.eddicorp.member.repository.MemberRepository;
import com.eddicorp.member.repository.MemberRepositoryImpl;
import com.eddicorp.session.Cookie;
import com.eddicorp.session.repository.SessionRepository;
import com.eddicorp.session.repository.SessionRepositoryImpl;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository = MemberRepositoryImpl.getInstance();
    private final SessionRepository sessionRepository = SessionRepositoryImpl.getInstance();

    @Override
    public void signUp(String username, String password) {
        final Member member = new Member(username, password);
        memberRepository.signUp(member);
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public Boolean signIn(String username, String password) {
        Cookie cookie = sessionRepository.findCookieByUsername(username);

        final Member member = memberRepository.findByUsername(username);

        if (member.getPassword().equals(password)) {
            return true;
        }

        return false;
    }
}
