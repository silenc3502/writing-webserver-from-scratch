package com.eddicorp.parsing;

import com.eddicorp.WebApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@DisplayName("문자열 파싱")
public class ParsingTest {

    String filename = "parse-test.txt";
    byte[] rawFileToServe;

    String convertedString;

    public void prepare() throws IOException {
        rawFileToServe = readFileFromResourceStream(filename);
        convertedString = new String(rawFileToServe);
    }

    public static byte[] readFileFromResourceStream(String fileName) throws IOException {
        final String filePath = Paths.get("text", fileName).toString();
        try (
                final InputStream resourceInputStream = WebApplication.class
                        .getClassLoader()
                        .getResourceAsStream(filePath);
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ) {
            if (resourceInputStream == null) {
                return null;
            }

            int readCount = 0;
            final byte[] readBuffer = new byte[8192];
            while ((readCount = resourceInputStream.read(readBuffer)) != -1) {
                baos.write(readBuffer, 0, readCount);
            }
            return baos.toByteArray();
        }
    }

    @Test
    void txt_파일_파싱_테스트() throws IOException {
        prepare();

        System.out.println(convertedString);
    }
}
