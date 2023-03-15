package com.eddicorp.controller;

import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;
import com.eddicorp.http.HttpStatus;
import com.eddicorp.member.service.MemberService;
import com.eddicorp.member.service.MemberServiceImpl;
import com.eddicorp.network.Controller;

public class SignUpController implements Controller {

    private final MemberService userService = new MemberServiceImpl();

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        userService.signUp(username, password);

        response.setHeader("Location", "/login.html");
        response.setStatus(HttpStatus.FOUND);
        response.renderRawBody(null);
    }
}
