package com.eddicorp;

import com.eddicorp.blog.Post;
import com.eddicorp.blog.PostRequest;
import com.eddicorp.http.HttpMethod;
import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;
import com.eddicorp.member.Member;
import com.eddicorp.member.MemberRequest;
import com.eddicorp.network.WebRequestReceiver;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.*;

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