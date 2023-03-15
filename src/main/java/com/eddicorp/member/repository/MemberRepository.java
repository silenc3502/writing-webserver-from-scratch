package com.eddicorp.member.repository;

import com.eddicorp.member.Member;

public interface MemberRepository {

    void signUp(Member member);

    Member findByUsername(String username);
}
