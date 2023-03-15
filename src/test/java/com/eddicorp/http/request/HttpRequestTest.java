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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void 일부_파라미터_얻기() {

        final String sutString = "test";

        assertEquals(sutString, httpRequest.getParameter("title"));
    }

    @Test
    public void 파싱_불가능() throws IOException {
        final String requestMessage =
                "POST /api/signup HTTP/1.1\r";

        final byte[] requestBytes = requestMessage.getBytes(StandardCharsets.UTF_8);
        final InputStream byteArrayInputStream = new ByteArrayInputStream(requestBytes);
        assertThrows(IllegalStateException.class, () -> {
            new HttpRequest(byteArrayInputStream);
        });
    }

    @Test
    public void CRLF_찾지못함() {
        final String requestMessage =
                "POST /api/signup HTTP/1.1";

        final byte[] requestBytes = requestMessage.getBytes(StandardCharsets.UTF_8);
        final InputStream byteArrayInputStream = new ByteArrayInputStream(requestBytes);
        assertThrows(IllegalStateException.class, () -> {
            new HttpRequest(byteArrayInputStream);
        });
    }

    @Test
    public void 파싱할_body_없음() throws IOException {
        final String requestMessage =
                "POST /api/signup HTTP/1.1\r\n" +
                "\r\n";

        final byte[] requestBytes = requestMessage.getBytes(StandardCharsets.UTF_8);
        final InputStream byteArrayInputStream = new ByteArrayInputStream(requestBytes);
        httpRequest = new HttpRequest(byteArrayInputStream);
        Map<String, String> sutEmptyMap = new HashMap<>();

        assertEquals(sutEmptyMap, httpRequest.getParameterMap());
    }
}
