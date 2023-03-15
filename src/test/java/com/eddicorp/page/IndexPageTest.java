package com.eddicorp.page;

import com.eddicorp.member.Member;
import com.eddicorp.member.repository.MemberRepository;
import com.eddicorp.member.repository.MemberRepositoryImpl;
import com.eddicorp.member.service.MemberService;
import com.eddicorp.member.service.MemberServiceImpl;
import com.eddicorp.network.RequestHandler;
import com.eddicorp.session.repository.SessionRepository;
import com.eddicorp.session.repository.SessionRepositoryImpl;
import com.eddicorp.session.service.SessionService;
import com.eddicorp.session.service.SessionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@DisplayName("IndexPageTest test")
public class IndexPageTest {

    private OutputStream outputStream;
    private InputStream inputStream;
    private RequestHandler requestHandler;

    final private MemberService memberService = new MemberServiceImpl();
    final MemberRepository memberRepository = MemberRepositoryImpl.getInstance();
    private final SessionService sessionService = new SessionServiceImpl();
    final SessionRepository sessionRepository = SessionRepositoryImpl.getInstance();

    @Test
    public void test () {
        Member member = new Member("test", "test");
        memberService.signUp(member.getUsername(), member.getPassword());
        Boolean isLoginSuccess = memberService.signIn(member.getUsername(), member.getPassword());

        if (isLoginSuccess) {
            final String sessionId = UUID.randomUUID().toString();
            sessionService.setAttribute(sessionId, member);
        }
    }
}
