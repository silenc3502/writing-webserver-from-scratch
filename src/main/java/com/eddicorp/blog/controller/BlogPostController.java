package com.eddicorp.blog.controller;

import com.eddicorp.blog.service.BlogPostService;
import com.eddicorp.blog.service.BlogPostServiceImpl;
import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;
import com.eddicorp.http.HttpStatus;
import com.eddicorp.member.Member;
import com.eddicorp.router.Controller;
import com.eddicorp.session.Cookie;
import com.eddicorp.session.repository.SessionRepository;
import com.eddicorp.session.repository.SessionRepositoryImpl;

public class BlogPostController implements Controller {

    private final SessionRepository sessionRepository = SessionRepositoryImpl.getInstance();
    private final BlogPostService postService = new BlogPostServiceImpl();

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        final String title = request.getParameter("title");
        final String content = request.getParameter("content");

        String sessionId = null;
        Member member = null;

        final Cookie cookie = request.getCookie("JSESSIONID");
        if (cookie != null) {
            sessionId = cookie.getValue();
            member = (Member) sessionRepository.getAttribute(sessionId);
        }

        //final HttpSession maybeSession = request.getSession();
        String username;
        if (member == null) {
            username = "익명";
        } else {
            username = member.getUsername();
        }

        postService.write(title, content, username);
        response.setHeader("Location", "/");
        response.setStatus(HttpStatus.FOUND);
        response.renderRawBody(null);
    }
}
