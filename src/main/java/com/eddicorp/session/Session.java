package com.eddicorp.session;

import com.eddicorp.member.Member;

public class Session {

    private final String sequence;
    private Member member;

    public Session(String sequence, Member member) {
        this.sequence = sequence;
        this.member = member;
    }

    public String getSequence() {
        return sequence;
    }

    public Member getMember() {
        return member;
    }
}
