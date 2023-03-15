package com.eddicorp.http.response;

import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;
import com.eddicorp.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("HttpResponse test")
public class HttpResponseTest {

    private OutputStream outputStream;
    private HttpResponse httpResponse;

    @BeforeEach
    public void 초기_설정() throws IOException {
        outputStream = new ByteArrayOutputStream();
        httpResponse = new HttpResponse(outputStream);
        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.setHeader("Content-Length", "0");
        httpResponse.setHeader("Location", "http://localhost:8787/");
    }

    @Test
    public void 응답_확인() {
        String rawBody = "<html>\r\n" +
                "  <head>\r\n" +
                "    <title>An Example Page</title>\r\n" +
                "  </head>\r\n" +
                "  <body>\r\n" +
                "    <p>Hello World, this is a very simple HTML document.</p>\r\n" +
                "  </body>\r\n" +
                "</html>";

        httpResponse.renderRawBody(rawBody.getBytes());
        
        final String sutResponseString = "HTTP/1.1 302 Found\r\n" +
                "Content-Length: 0\r\n" +
                "Location: http://localhost:8787/\r\n" +
                "\r\n" +
                "<html>\r\n" +
                "  <head>\r\n" +
                "    <title>An Example Page</title>\r\n" +
                "  </head>\r\n" +
                "  <body>\r\n" +
                "    <p>Hello World, this is a very simple HTML document.</p>\r\n" +
                "  </body>\r\n" +
                "</html>";

        assertEquals(sutResponseString, outputStream.toString());
    }

    @Test
    public void 파일_예외처리_확인() throws IOException {

        final String response = "HTTP/1.1 302 Found\r\n" +
                "Content-Length: 0\r\n" +
                "Location: http://localhost:8787/\r\n" +
                "\r\n";

        final byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        final File fileToExport = new File("./src/main/resources/cantMake/response.txt");

        outputStream = new FileOutputStream(fileToExport);
        httpResponse = new HttpResponse(outputStream);
        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.setHeader("Content-Length", "0");
        httpResponse.setHeader("Location", "http://localhost:8787/");
        outputStream.close();

        assertThrows(RuntimeException.class, () -> {
            httpResponse.renderRawBody(null);
        });
    }
}
