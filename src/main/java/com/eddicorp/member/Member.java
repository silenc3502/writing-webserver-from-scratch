package com.eddicorp.member;

public class Member {

    private Long memberId;
    private String username;
    private String password;

    public Member(Long memberId, String username, String password) {
        this.memberId = memberId++;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
