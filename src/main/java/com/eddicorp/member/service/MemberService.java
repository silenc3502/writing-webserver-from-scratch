package com.eddicorp.member.service;

import com.eddicorp.member.Member;

public interface MemberService {

    void signUp(String username, String password);

    Member findByUsername(String username);

    Boolean signIn(String test, String test1);
}
