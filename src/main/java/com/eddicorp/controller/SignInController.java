package com.eddicorp.controller;

import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;
import com.eddicorp.http.HttpStatus;
import com.eddicorp.member.Member;
import com.eddicorp.member.service.MemberService;
import com.eddicorp.member.service.MemberServiceImpl;
import com.eddicorp.network.Controller;
import com.eddicorp.session.repository.SessionRepositoryImpl;
import com.eddicorp.session.response.CookieResponse;
import com.eddicorp.session.service.SessionService;
import com.eddicorp.session.service.SessionServiceImpl;

import java.util.UUID;

public class SignInController implements Controller {

    private final MemberService memberService = new MemberServiceImpl();
    private final SessionService sessionService = new SessionServiceImpl();

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final Member foundUser = memberService.findByUsername(username);

        System.out.println("password = " + password);
        System.out.println("foundUser = " + foundUser);

        final Boolean isLoginSuccess = memberService.signIn(username, password);

        if (isLoginSuccess) {
            // session Id 설정
            // 속성 지정
            //sessionService.setAttribute("USER", foundUser);
            //final SessionService session = request.getSession(true);
            //session.setAttribute("USER", foundUser);
            //final String sessionId = session.getId();
            final String sessionId = UUID.randomUUID().toString();
            sessionService.setAttribute(sessionId, foundUser);

            final CookieResponse cookieResponse = new CookieResponse(
                    SessionRepositoryImpl.SESSION_KEY_NAME,
                    sessionId,
                    "/",
                    "localhost",
                    60 * 5
            );
            response.setHeader("Set-Cookie", cookieResponse.build());
            response.setHeader("Location", "/");
            response.setStatus(HttpStatus.FOUND);
            response.renderRawBody(null);
        }
    }
}
