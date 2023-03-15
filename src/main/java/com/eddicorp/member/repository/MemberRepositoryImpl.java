package com.eddicorp.member.repository;

import com.eddicorp.member.Member;

import java.util.HashMap;
import java.util.Map;

public class MemberRepositoryImpl implements MemberRepository {

    private static final MemberRepository INSTANCE = new MemberRepositoryImpl();

    public static MemberRepository getInstance() {
        return INSTANCE;
    }

    private final Map<String, Member> memberDb = new HashMap<>();

    private MemberRepositoryImpl() {
    }

    @Override
    public void signUp(Member member) {
        memberDb.put(member.getUsername(), member);
    }

    @Override
    public Member findByUsername(String username) {
        return memberDb.get(username);
    }
}
