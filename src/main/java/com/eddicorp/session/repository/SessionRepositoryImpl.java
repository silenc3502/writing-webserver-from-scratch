package com.eddicorp.session.repository;

import com.eddicorp.member.Member;
import com.eddicorp.session.Cookie;
import com.eddicorp.session.Session;
import com.eddicorp.session.service.SessionService;
import com.eddicorp.session.service.SessionServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionRepositoryImpl implements SessionRepository {

    public static final String SESSION_KEY_NAME = "JSESSIONID";
    private static final SessionRepository INSTANCE = new SessionRepositoryImpl();
    public static SessionRepository getInstance() {
        return INSTANCE;
    }
    private static final Map<String, Object> sessionMap = new HashMap<>();
    private static final Map<String, Cookie> cookieMap = new HashMap<>();

//    @Override
//    public void removeSession(String id) {
//        sessionRepositoryMap.remove(id);
//    }

    @Override
    public void removeSession(String id) {

    }

    @Override
    public void createNewSession(Member member) {
        final String sessionSequence = UUID.randomUUID().toString();
        //cookieMap.put(sessionSequence, )
        //sessionMap.put(sessionSequence, member);
    }

    @Override
    public SessionService createSession(String id) {
        return null;
    }

    @Override
    public SessionService getSession(Member member) {
        return null;
    }

//    @Override
//    public SessionService getSession(String id) {
//        final SessionService maybeHttpSession = sessionRepositoryMap.get(id);
//        if (maybeHttpSession == null) {
//            return createSession(id);
//        }
//        return maybeHttpSession;
//    }

    @Override
    public void setAttribute(String name, Object value) {
        sessionMap.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return sessionMap.get(name);
    }

    @Override
    public void removeAttribute(String name) {
        sessionMap.remove(name);
    }

    @Override
    public Object findSessionByUsername(String username) {
        return sessionMap.get(username);
    }

    @Override
    public Cookie findCookieByUsername(String username) {
        return cookieMap.get(username);
    }
}
