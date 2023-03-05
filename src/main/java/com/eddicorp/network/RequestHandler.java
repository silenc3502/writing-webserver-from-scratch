package com.eddicorp.network;

import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RequestHandler {

    private final NetworkMainController networkMainController = new NetworkMainController();

    public void handle(final InputStream inputStream, OutputStream outputStream) {
        try {
            final HttpRequest httpRequest = new HttpRequest(inputStream);
            final HttpResponse httpResponse = new HttpResponse(outputStream);
            networkMainController.handle(httpRequest, httpResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
