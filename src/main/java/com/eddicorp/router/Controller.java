package com.eddicorp.router;

import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;

public interface Controller {

    public void handle(HttpRequest request, HttpResponse response);
}
