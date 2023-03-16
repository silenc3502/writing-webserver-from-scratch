package com.eddicorp;

import com.eddicorp.router.WebRequestReceiver;

import java.net.ServerSocket;
import java.net.Socket;

public class WebApplication {

    public static void main(String[] args) throws Exception {

        final ServerSocket serverSocket = new ServerSocket(8787);
        Socket clientSocket;
        while ((clientSocket = serverSocket.accept()) != null) {
            final WebRequestReceiver receiver = new WebRequestReceiver(clientSocket);
            receiver.receiveData();
        }
    }
}