package com.eddicorp.controller;

import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;
import com.eddicorp.http.HttpStatus;
import com.eddicorp.network.Controller;
import com.eddicorp.session.Session;
import com.eddicorp.session.repository.SessionRepository;
import com.eddicorp.session.repository.SessionRepositoryImpl;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class IndexController implements Controller {

    private static final String STATIC_FILE_PATH = "pages";
    private final Mustache.Compiler compiler = Mustache.compiler();
    private Template template = null;
    //private final SessionRepository sessionRepository = SessionRepositoryImpl.getInstance();

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        final String pathToLoad = Paths.get(STATIC_FILE_PATH, "index.html").toString();
        try (
                final InputStream staticFileInputStream = this.getClass().getClassLoader().getResourceAsStream(pathToLoad);
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ) {
            if (template == null) {
                final String templateString = readRawContentFromStaticFile(staticFileInputStream, byteArrayOutputStream);
                this.template = compiler.compile(templateString);
            }

            final Map<String, Object> context = new HashMap<>();
            //JSESSIONID
            //final String sessionString = request.getCookie("JSESSIONID").getValue();

//            if (sessionString != null) {
//                context.put("isLoggedIn", true);
//            } else {
//                context.put("isLoggedIn", false);
//            }

            final String rendered = template.execute(context);
            final byte[] rawContent = rendered.getBytes(StandardCharsets.UTF_8);
            response.setHeader("Content-Type", "text/html;charset=utf-8");
            response.setHeader("Content-Length", String.valueOf(rawContent.length));
            response.setStatus(HttpStatus.OK);
            response.renderRawBody(rawContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readRawContentFromStaticFile(InputStream staticFileInputStream, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        int readCount;
        final byte[] buffer = new byte[8192];
        while ((readCount = staticFileInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, readCount);
        }
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
    }
}
