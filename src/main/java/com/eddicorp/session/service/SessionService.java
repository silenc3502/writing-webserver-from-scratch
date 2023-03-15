package com.eddicorp.session.service;

public interface SessionService {
    String getId();

    void setAttribute(String name, Object object);

    Object getAttribute(String name);

    void removeAttribute(String name);

    void invalidate();
}
