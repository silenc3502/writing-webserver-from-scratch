package com.eddicorp.router;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class WebRequestReceiver {

    private final Socket clientSocket;
    private final RequestHandler requestHandler = new RequestHandler();

    public WebRequestReceiver(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void receiveData() {
        try (
                final InputStream inputStream = clientSocket.getInputStream();
                final OutputStream outputStream = clientSocket.getOutputStream();
        ) {
            requestHandler.handle(inputStream, outputStream);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.err.println(throwable);
        }
    }
}
