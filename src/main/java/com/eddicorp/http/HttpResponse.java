package com.eddicorp.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {

    private final OutputStream outputStream;
    private HttpStatus httpStatus;
    private final Map<String, String> headerMap = new HashMap<>();
    private byte[] body;

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setHeader(String headerName, String headerValue) {
        this.headerMap.put(headerName, headerValue);
    }

    public void renderRawBody(byte[] rawBody) {
        try {
            final byte[] rawResponseLineAndHeaders = buildResponseLineAndHeadersInRawBytes();
            outputStream.write(rawResponseLineAndHeaders);
            if (rawBody != null) {
                outputStream.write(rawBody);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void flush() throws IOException {
        buildStatusLine();
        buildHeaders();
        outputStream.write(CRLF.getBytes(StandardCharsets.UTF_8));
        outputStream.write(body);
        outputStream.flush();
    }

    public void postFlush() throws IOException {
        buildStatusLine();
        buildHeaders();
        outputStream.write(CRLF.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    private static final String CRLF = "\r\n";

    private void buildHeaders() throws IOException {
        for (Map.Entry<String, String> entry : this.headerMap.entrySet()) {
            final String headerName = entry.getKey();
            final String headerValue = entry.getValue();
            final String header = headerName + ": " + headerValue + CRLF;
            outputStream.write(header.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void buildStatusLine() throws IOException {
        final String statusLine = "HTTP/1.1 " + this.httpStatus.statusCode + " " + this.httpStatus.reasonPhrase + CRLF;
        outputStream.write(statusLine.getBytes(StandardCharsets.UTF_8));
    }

    public enum HttpStatus {
        OK(200, "OK"),
        FOUND(302, "Found");

        private final int statusCode;
        private final String reasonPhrase;

        HttpStatus(int statusCode, String reasonPhrase) {
            this.statusCode = statusCode;
            this.reasonPhrase = reasonPhrase;
        }

        public int getHttpStatusCode() {
            return statusCode;
        }

        public String getReasonPhrase() {
            return reasonPhrase;
        }
    }

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    private byte[] buildResponseLineAndHeadersInRawBytes() {
        Objects.requireNonNull(httpStatus);
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("HTTP/1.1").append(" ")
                .append(httpStatus.getHttpStatusCode()).append(" ")
                .append(httpStatus.getReasonPhrase()).append(CRLF);

        for (Map.Entry<String, String> headerMapEntry : headerMap.entrySet()) {
            stringBuilder
                    .append(headerMapEntry.getKey()).append(": ")
                    .append(headerMapEntry.getValue())
                    .append(CRLF);
        }

        stringBuilder.append(CRLF);
        final String responseLineAndHeaders = stringBuilder.toString();
        final byte[] rawResponseLineAndHeaders = responseLineAndHeaders.getBytes(StandardCharsets.UTF_8);
        return rawResponseLineAndHeaders;
    }
}
