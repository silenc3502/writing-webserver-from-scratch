package com.eddicorp.receive;

import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;
import com.eddicorp.router.Controller;
import com.eddicorp.router.StaticFileController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.file.Paths;

@DisplayName("URL 요청 수신에 대한 테스트")
public class ReceiveURLRequestTest {

    private static final String STATIC_FILE_PATH = "pages";
    @Test
    void URLDecode_테스트() throws IOException {

        final ServerSocket serverSocket = new ServerSocket(8787);
        Socket clientSocket;
        clientSocket = serverSocket.accept();

        final InputStream inputStream = clientSocket.getInputStream();
        final OutputStream outputStream = clientSocket.getOutputStream();

        final HttpRequest httpRequest = new HttpRequest(inputStream);
        final HttpResponse httpResponse = new HttpResponse(outputStream);

        final String uri = httpRequest.getUri();
        final String pathToLoad = Paths.get(STATIC_FILE_PATH, uri).toString();
        System.out.println("pathToLoad = " + pathToLoad);
        final URL maybeResource = this.getClass().getClassLoader().getResource(pathToLoad);
        System.out.println("maybeResource = " + maybeResource);

        Controller controller = new StaticFileController();
        controller.handle(httpRequest, httpResponse);
    }
}
