package com.eddicorp.controller;

import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;
import com.eddicorp.http.HttpStatus;
import com.eddicorp.network.Controller;
import com.eddicorp.session.Cookie;
import com.eddicorp.session.repository.SessionRepository;
import com.eddicorp.session.repository.SessionRepositoryImpl;

public class LogoutController implements Controller {

    private final SessionRepository sessionRepository = SessionRepositoryImpl.getInstance();

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        final Cookie cookie = request.getCookie("JSESSIONID");
        final String sessionId = cookie.getValue();

        request.clearCookie("JSESSIONID");
        sessionRepository.removeAttribute(sessionId);

        response.setHeader("Location", "/");
        response.setStatus(HttpStatus.FOUND);
        response.renderRawBody(null);
    }
}