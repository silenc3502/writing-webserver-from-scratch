package com.eddicorp.network;

import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;

import java.nio.file.Paths;

public class NotFoundController implements Controller {
    private static final String STATIC_FILE_PATH = "pages";

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        final String pathToLoad = Paths.get(STATIC_FILE_PATH, "not-found.html").toString();
    }
}
