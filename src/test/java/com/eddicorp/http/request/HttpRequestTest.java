package com.eddicorp.http.request;

import com.eddicorp.http.HttpMethod;
import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("HttpRequest test")
public class HttpRequestTest {

    private HttpRequest httpRequest;
    int formLength;

    @BeforeEach
    public void 초기_설정() throws IOException {
        final String formData = "title=test&content=test";
        formLength = formData.length();

        final String requestMessage =
                "POST /api/signup HTTP/1.1\r\n"  +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: " + formLength + "\r\n" +
                "\r\n" +
                formData;

        final byte[] requestBytes = requestMessage.getBytes(StandardCharsets.UTF_8);
        final InputStream byteArrayInputStream = new ByteArrayInputStream(requestBytes);
        httpRequest = new HttpRequest(byteArrayInputStream);
    }

    @Test
    public void HTTP_매서드_얻기() {
        final HttpMethod sutHttpMethod = HttpMethod.POST;
        assertEquals(sutHttpMethod, httpRequest.getHttpMethod());
    }

    @Test
    public void HTTP_uri_얻기() {
        final String sutUri = "/api/signup";
        assertEquals(sutUri, httpRequest.getUri());
    }

    @Test
    public void HTTP_헤더_얻기() {
        Map<String, String> sutHeader = new HashMap<>();
        sutHeader.put("Content-Type", "application/x-www-form-urlencoded");
        sutHeader.put("Content-Length", Integer.toString(formLength));

        assertEquals(sutHeader, httpRequest.getHeaderMap());
    }

    @Test
    public void body_얻기() {

        Map<String, String> sutBodyParameter = new HashMap<>();
        sutBodyParameter.put("title", "test");
        sutBodyParameter.put("content", "test");

        assertEquals(sutBodyParameter, httpRequest.getParameterMap());
    }
}
