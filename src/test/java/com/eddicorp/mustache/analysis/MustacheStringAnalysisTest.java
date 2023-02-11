package com.eddicorp.mustache.analysis;

import com.eddicorp.WebApplication;
import com.eddicorp.util.MustacheStringReplacer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DisplayName("Mustache 문자열 분석")
public class MustacheStringAnalysisTest {

    String filename = "index.html";
    byte[] rawFileToServe;

    String convertedString;

    public void prepare() throws IOException {
        rawFileToServe = readFileFromResourceStream(filename);
        convertedString = new String(rawFileToServe);
        MustacheStringReplacer.analysisMustacheString(convertedString);
    }

    public static byte[] readFileFromResourceStream(String fileName) throws IOException {
        final String filePath = Paths.get("pages", fileName).toString();
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
    void mustache_문자열_파싱할_리스트_체크() throws IOException {
        prepare();

        List<Integer> start = MustacheStringReplacer.getStartIdx();
        List<Integer> end = MustacheStringReplacer.getEndIdx();

        for (int i = 0; i < start.size(); i++) {
            System.out.println("start: " + start.get(i) + ", end: " + end.get(i));
        }
    }

    @Test
    void mustache_변환할_문자열_검사() throws IOException {
        prepare();

        List<String> mustacheString = MustacheStringReplacer.getMustacheString();

        for (int i = 0; i < mustacheString.size(); i++) {
            System.out.println(mustacheString.get(i));
        }
    }

    @Test
    void mustache_문자_변환() throws IOException {
        prepare();

        Map<String, String> mustacheMapData = new HashMap<>();
        mustacheMapData.put("title", "let's gogogo!!!");
        mustacheMapData.put("content", "what");
        mustacheMapData.put("author", "gogosing");

        List<Integer> startIdx = MustacheStringReplacer.getStartIdx();
        List<Integer> endIdx = MustacheStringReplacer.getEndIdx();
        List<String> mustacheString = MustacheStringReplacer.getMustacheString();

        String backupString = convertedString;
        int lengthDifference = 0;
        int matchingNumber = 0;

        for (int i = 0; i < mustacheString.size(); i++) {
            for (Map.Entry<String, String> entrySet : mustacheMapData.entrySet()) {
                if (mustacheString.get(i).equals(entrySet.getKey())) {

                    String tmpString = backupString.substring(0, startIdx.get(i)
                            - (MustacheStringReplacer.BRACE_START + MustacheStringReplacer.BRACE_END) * matchingNumber
                            + lengthDifference * (matchingNumber > 0 ? 1 : 0));

                    backupString = tmpString
                            + entrySet.getValue()
                            + convertedString.substring(endIdx.get(i) + MustacheStringReplacer.BRACE_END, convertedString.length());

                    lengthDifference += entrySet.getValue().length() - entrySet.getKey().length();
                    matchingNumber++;
                }
            }
        }

        System.out.println(backupString);
    }
}
