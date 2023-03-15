package com.eddicorp.session.service;

import com.eddicorp.member.repository.MemberRepository;
import com.eddicorp.member.repository.MemberRepositoryImpl;
import com.eddicorp.session.repository.SessionRepository;
import com.eddicorp.session.repository.SessionRepositoryImpl;

import java.util.HashMap;
import java.util.Map;

public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository = SessionRepositoryImpl.getInstance();

    public SessionServiceImpl() {
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {
        sessionRepository.setAttribute(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return sessionRepository.getAttribute(name);
    }

    @Override
    public void removeAttribute(String name) {
        sessionRepository.removeAttribute(name);
    }

    @Override
    public void invalidate() {
        //sessionRepository.removeSession(id);
    }
}
