package com.eddicorp.http;

import java.io.IOException;
import java.io.InputStream;

public class HttpRequest {
    private final String uri;

    public String getUri() {
        return uri;
    }

    private final HttpMethod httpMethod;

    public HttpRequest(InputStream inputStream) throws IOException {
        final String rawRequestLine = readLine(inputStream);
        final String[] partsOfRequestLine = rawRequestLine.split(" ");
        this.uri = partsOfRequestLine[1];

        httpMethod = HttpMethod.checkHttpMethod(partsOfRequestLine[0]);
        System.out.println("HTTP METHOD: " + httpMethod);
    }

    private static String readLine(InputStream inputStream) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        int readCharacter;
        while ((readCharacter = inputStream.read()) != -1) {
            final char currentChar = (char) readCharacter;
            if (currentChar == '\r') {
                if (((char) inputStream.read()) == '\n') {
                    return stringBuilder.toString();
                } else {
                    throw new IllegalStateException("Invalid HTTP request.");
                }
            }
            stringBuilder.append(currentChar);
        }
        throw new IllegalStateException("Unable to find CRLF");
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
}
