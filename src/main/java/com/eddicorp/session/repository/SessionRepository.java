package com.eddicorp.session.repository;

import com.eddicorp.member.Member;
import com.eddicorp.session.Cookie;
import com.eddicorp.session.service.SessionService;

public interface SessionRepository {

    public void removeSession(String id);

    public void createNewSession(Member member);

    public SessionService createSession(String id);

    public SessionService getSession(Member member);

    void setAttribute(String name, Object value);

    Object getAttribute(String name);

    void removeAttribute(String name);

    Object findSessionByUsername(String username);

    Cookie findCookieByUsername(String username);
}
