package com.eddicorp.http;

public enum HttpMethod {
    OPTIONS,
    GET,
    POST,
    PUT,
    DELETE,
    HEAD,
    TRACE,
    CONNECT,
    PATCH;

    public static HttpMethod checkHttpMethod(String requestedHttpMethod) {
        if (requestedHttpMethod.equals("OPTIONS")) {
            return HttpMethod.OPTIONS;
        }
        if (requestedHttpMethod.equals("GET")) {
            return HttpMethod.GET;
        }
        if (requestedHttpMethod.equals("POST")) {
            return HttpMethod.POST;
        }
        if (requestedHttpMethod.equals("PUT")) {
            return HttpMethod.PUT;
        }
        if (requestedHttpMethod.equals("DELETE")) {
            return HttpMethod.DELETE;
        }
        if (requestedHttpMethod.equals("HEAD")) {
            return HttpMethod.HEAD;
        }
        if (requestedHttpMethod.equals("TRACE")) {
            return HttpMethod.TRACE;
        }
        if (requestedHttpMethod.equals("CONNECT")) {
            return HttpMethod.CONNECT;
        }
        if (requestedHttpMethod.equals("PATCH")) {
            return HttpMethod.PATCH;
        }

        return null;
    }
}
