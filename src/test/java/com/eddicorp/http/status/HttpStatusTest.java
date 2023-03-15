package com.eddicorp.http.status;

import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("HttpStatus test")
public class HttpStatusTest {

    @Test
    public void StatusCode_확인() {
        HttpStatus sutHttpStatus = HttpStatus.OK;

        assertEquals(200, sutHttpStatus.getHttpStatusCode());
    }

    @Test
    public void reasonPhrase_확인() {
        HttpStatus sutHttpStatus = HttpStatus.OK;

        assertEquals("OK", sutHttpStatus.getReasonPhrase());
    }

}
