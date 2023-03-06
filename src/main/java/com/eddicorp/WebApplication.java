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

    public static List<List<Map<String, String>>> jsonMappedDataList = new ArrayList<>();
    public static List<Member> memberList = new ArrayList<>();
    public static Map<String, String> cookieMap = new HashMap<>();
    public static List<Post> posts = new ArrayList<>();
    public static Boolean loginCheck = false;

    public static Mustache.Compiler compiler = Mustache.compiler();

    public static Map<String, Object> context = new HashMap<>();

    public static void main(String[] args) throws Exception {

        final ServerSocket serverSocket = new ServerSocket(8787);
        Socket clientSocket;
        while ((clientSocket = serverSocket.accept()) != null) {
            final WebRequestReceiver receiver = new WebRequestReceiver(clientSocket);
            receiver.receiveData();
        }

        // index.html 브라우저에 띄워보기
        final ServerSocket serverSocket2 = new ServerSocket(8787);
        Socket connection;
        while ((connection = serverSocket2.accept()) != null) {
            try (
                    final InputStream inputStream = connection.getInputStream();
                    final OutputStream outputStream = connection.getOutputStream()
            ) {
                // 요청을 분석해서 적절한 응답을 주자
                final HttpRequest httpRequest = new HttpRequest(inputStream);
                final String uri = httpRequest.getUri();

                String filename;
                if ("/".equals(uri)) {
                    filename = "index.html";
                } else {
                    filename = uri;
                }

                String extension = null;
                final int indexOfPeriod = uri.lastIndexOf(".");
                if (indexOfPeriod != -1) {
                    extension = uri.substring(indexOfPeriod + 1);
                }
                System.out.println("extension = " + extension);

                // fallback으로 기본 값 지정
                String mimeType = "text/html; charset=utf-8";
                if ("html".equals(extension)) {
                    mimeType = "text/html; charset=utf-8";
                }

                if ("css".equals(extension)) {
                    mimeType = "text/css; charset=utf-8";
                }

                if ("svg".equals(extension)) {
                    mimeType = "image/svg+xml";
                }

                if ("ico".equals(extension)) {
                    mimeType = "image/x-icon";
                }

                System.out.println("filename: " + filename);

                if (httpRequest.getHttpMethod() == HttpMethod.POST) {
                    if (filename.equals("/users")) {
                        // HTTP METHOD: POST
                        // extension = null
                        // filename: /users
                        // redirect: http://localhost:8787/login.html

                        MemberRequest memberRequest = readRemainJsonStreamForMemberRequest(inputStream);
                        Member member = new Member((long) memberList.size(), memberRequest.getUsername(), memberRequest.getPassword());
                        memberList.add(member);

                        System.out.println("회원 가입이 완료되었습니다!");
                        System.out.println("로그인을 진행해주세요.");

                        HttpResponse httpResponse = new HttpResponse(outputStream);
                        httpResponse.setHttpStatus(HttpResponse.HttpStatus.FOUND);
                        httpResponse.setHeader("Content-Length", "0");
                        httpResponse.setHeader("Location", "http://localhost:8787/login.html");
                        httpResponse.postFlush();
                    } else if (filename.equals("/login")) {
                        // HTTP METHOD: POST
                        // extension = null
                        // filename: /login
                        // redirect: http://localhost:8787/

                        boolean isLoginSuccess = false;
                        MemberRequest memberRequest = readRemainJsonStreamForMemberRequest(inputStream);

                        HttpResponse httpResponse = new HttpResponse(outputStream);
                        httpResponse.setHttpStatus(HttpResponse.HttpStatus.FOUND);
                        httpResponse.setHeader("Content-Length", "0");
                        httpResponse.setHeader("Location", "http://localhost:8787/");

                        isLoginSuccess = login(memberRequest);
                        if (isLoginSuccess) {
                            System.out.println("로그인 성공!");

                            String cookie = UUID.randomUUID().toString();
                            cookieMap.put(cookie, memberRequest.getUsername());

                            System.out.println("Cookie: " + cookie);
                            System.out.println("Cookie length: " + cookie.length());

                            httpResponse.setHeader("Set-Cookie",
                                    "JSESSIONID=" + cookie + "; Path=/; Domain=localhost; Max-Age=300");

                            //context.put("isLoggedIn", true);
                            loginCheck = true;
                        } else {
                            System.out.println("로그인 실패");

                            //context.put("isLoggedIn", false);
                            loginCheck = false;
                        }

                        httpResponse.postFlush();
                    } else if (filename.equals("/post")) {
                        PostRequest postRequest = readRemainJsonStreamForPostRequest(inputStream);
                        Post post = new Post((long) posts.size(), postRequest.getTitle(), postRequest.getContent(), postRequest.getAuthor());

                        posts.add(post);

                        HttpResponse httpResponse = new HttpResponse(outputStream);
                        httpResponse.setHttpStatus(HttpResponse.HttpStatus.FOUND);
                        httpResponse.setHeader("Content-Length", "0");
                        httpResponse.setHeader("Location", "http://localhost:8787/");
                        httpResponse.postFlush();
                    }


                    /*
                    readRemainJsonStream(inputStream);

                    System.out.println("finish to read formed data!");

                    HttpResponse httpResponse = new HttpResponse(outputStream);
                    httpResponse.setHttpStatus(HttpResponse.HttpStatus.FOUND);
                    httpResponse.setHeader("Content-Length", "0");
                    httpResponse.setHeader("Location", "http://localhost:8787/");
                    httpResponse.postFlush();

                    //JSONParser parser = new JSONParser();
                    //JSONObject jsonObject = (JSONObject) parser.parse();
                     */
                } else {
                    if (filename.equals("/logout")) {
                        String cookie = readRemainJsonStreamForLogout(inputStream);
                        cookieMap.remove(cookie);

                        if (!cookie.equals(null)) {
                            loginCheck = false;
                        }

                        HttpResponse httpResponse = new HttpResponse(outputStream);
                        httpResponse.setHttpStatus(HttpResponse.HttpStatus.FOUND);
                        httpResponse.setHeader("Content-Length", "0");
                        httpResponse.setHeader("Location", "http://localhost:8787/");
                        httpResponse.postFlush();
                    } else {
                        // 이대로라면 문제가 생김. mime type을 지정해주지 않았기 때문에!
                        byte[] rawFileToServe = readFileFromResourceStream(filename);

                        String servedString = new String(rawFileToServe);

                        System.out.println("isLoggedIn: " + context.put("isLoggedIn", true));

                        if (filename.matches(".*\\.html")) {
                            rawFileToServe = runMustacheTemplateEngine(servedString);
                        }
                        //System.out.println(new String(rawFileToServe));


                        //rawFileToServe = replaceMustacheData(servedString);

                        // 응답 만들기
                        final HttpResponse httpResponse = new HttpResponse(outputStream);
                        // 1. 상태라인
                        // - HTTP/1.1 200 OK
                        httpResponse.setHttpStatus(HttpResponse.HttpStatus.OK);
                        // 2. 헤더
                        // - Content-Type: text/html; charset=utf-8
                        httpResponse.setHeader("Content-Type", mimeType);
                        // - Content-Length: rawFileToServe.length
                        httpResponse.setHeader("Content-Length", String.valueOf(rawFileToServe.length));
                        // 3. 바디
                        httpResponse.setBody(rawFileToServe);

                        // 응답하기!
                        httpResponse.flush();
                    }
                }
            }
        }
    }

    private static String readRemainJsonStreamForLogout(InputStream inputStream) throws IOException {

        int readCount = 0;
        byte[] readBuffer = new byte[8192];

        System.out.println("readRemainJsonStreamForLogout()");

        while ((readCount = inputStream.read(readBuffer)) != -1) {
            System.out.println("readCount: " + readCount);

            String convertedString = new String(readBuffer);
            //System.out.print(convertedString);

            final String[] cookieValue = convertedString.split("JSESSIONID=");
            System.out.println("cookieValue: " + cookieValue[1].trim());

            return cookieValue[1].trim();
        }

        return null;
    }

    private static PostRequest readRemainJsonStreamForPostRequest(InputStream inputStream) throws IOException {

        int readCount = 0;
        int findJsonTarget = 0;
        byte[] readBuffer = new byte[8192];

        PostRequest postRequest = new PostRequest();

        List<Integer> crlfList = new ArrayList<>();
        Boolean acquireRequestObject = false;

        System.out.println("readRemainJsonStreamForPostRequest()");

        while ((readCount = inputStream.read(readBuffer)) != -1) {
            System.out.println("readCount: " + readCount);

            String convertedString = new String(readBuffer);
            //System.out.print(convertedString);



            for (;;) {
                findJsonTarget = convertedString.indexOf("\r\n", findJsonTarget);
                crlfList.add(findJsonTarget);

                if (findJsonTarget == -1) break;

                System.out.println("CRLF location: " + findJsonTarget++);
            }

            for (int i = 0; i < crlfList.size() - 1; i++) {
                if (crlfList.get(i) + 2 == crlfList.get(i + 1)) {
                    System.out.println("find json target!");

                    List<String> keyValueParameter = new ArrayList<>();
                    String targetFormData = convertedString.substring(crlfList.get(i + 1) + 2);

                    System.out.println("form data: " + targetFormData);

                    final String[] separatedFormDataParameters = targetFormData.split("&");

                    for (int j = 0; j < separatedFormDataParameters.length; j++) {
                        final String[] tmpKeyValue = separatedFormDataParameters[j].split("=");
                        keyValueParameter.add(URLDecoder.decode(tmpKeyValue[1].trim(), "UTF-8"));

                        System.out.println("key: " + tmpKeyValue[0] + ", value = " + tmpKeyValue[1].trim());
                        System.out.println("key: " + URLDecoder.decode(tmpKeyValue[0], "UTF-8") + ", value = " + URLDecoder.decode(tmpKeyValue[1].trim(), "UTF-8"));
                    }

                    postRequest.setTitle(keyValueParameter.get(0));
                    postRequest.setContent(keyValueParameter.get(1));

                    if (loginCheck == false) { postRequest.setAuthor("anonymous"); }
                    else {
                        final String[] cookieValue = convertedString.split("JSESSIONID=");
                        String cookie = cookieValue[1].trim();
                        cookie = cookie.substring(0, 36);
                        System.out.println("cookieValue: " + cookie);
                        System.out.println("cookieValue Length: " + cookie.length());

                        String username = cookieMap.get(cookie);
                        System.out.println("cookie: " + cookie + ", username: " + username);
                        postRequest.setAuthor(username);
                    }

                    acquireRequestObject = true;
                }
            }

            if (acquireRequestObject) return postRequest;
        }

        return postRequest;
    }

    private static Boolean login (MemberRequest memberRequest) {

        System.out.println("username: " + memberRequest.getUsername() + ", password: " + memberRequest.getPassword());

        Member tmpMember;
        for (int i = 0; i < memberList.size(); i++) {

            tmpMember = memberList.get(i);
            if (tmpMember.getUsername().equals(memberRequest.getUsername())) {
                if (tmpMember.getPassword().equals(memberRequest.getPassword())) { return true; }
            }
        }

        return false;
    }

    private static byte[] readFileFromResourceStream(String fileName) throws IOException {
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

    private static byte[] runMustacheTemplateEngine(String mustacheTemplateString) {
        final Template template =
                compiler.compile(mustacheTemplateString);

        final Map<String, Object> context = new HashMap<>();
        context.put("posts", posts);
        context.put("isLoggedIn", loginCheck);
        final String renderedPage = template.execute(context);

        return renderedPage.getBytes();
    }

    private static MemberRequest readRemainJsonStreamForMemberRequest(InputStream inputStream) throws IOException {

        int readCount = 0;
        int findJsonTarget = 0;
        byte[] readBuffer = new byte[8192];

        MemberRequest memberRequest = new MemberRequest();

        List<Integer> crlfList = new ArrayList<>();
        Boolean acquireRequestObject = false;

        System.out.println("readRemainJsonStreamForMemberRequest()");

        while ((readCount = inputStream.read(readBuffer)) != -1) {
            System.out.println("readCount: " + readCount);
            //readBuffer[readCount] = '\0';
            // TODO:
            // 실제로 readLine에서 CRLF 나오는 라인 찾아서 그 아래만 처리하고 빠져나와도 무방한 상태임
            // form으로 전달되는 post data 처리

            String convertedString = new String(readBuffer);
            //System.out.print(convertedString);

            for (;;) {
                findJsonTarget = convertedString.indexOf("\r\n", findJsonTarget);
                crlfList.add(findJsonTarget);

                if (findJsonTarget == -1) break;

                System.out.println("CRLF location: " + findJsonTarget++);
            }

            for (int i = 0; i < crlfList.size() - 1; i++) {
                if (crlfList.get(i) + 2 == crlfList.get(i + 1)) {
                    System.out.println("find json target!");

                    List<String> keyValueParameter = new ArrayList<>();
                    String targetFormData = convertedString.substring(crlfList.get(i + 1) + 2);

                    System.out.println("form data: " + targetFormData);

                    final String[] separatedFormDataParameters = targetFormData.split("&");

                    for (int j = 0; j < separatedFormDataParameters.length; j++) {
                        final String[] tmpKeyValue = separatedFormDataParameters[j].split("=");
                        keyValueParameter.add(tmpKeyValue[1].trim());

                        System.out.println("key: " + tmpKeyValue[0] + ", value = " + tmpKeyValue[1].trim());
                    }

                    memberRequest.setUsername(keyValueParameter.get(0));
                    memberRequest.setPassword(keyValueParameter.get(1));

                    System.out.println("password check: " + keyValueParameter.get(1));

                    acquireRequestObject = true;
                }
            }

            if (acquireRequestObject) return memberRequest;
        }

        return memberRequest;
    }

    private static void readRemainJsonStream(InputStream inputStream) throws IOException {

        int readCount = 0;
        byte[] readBuffer = new byte[8192];

        String[] titleKeyValue;
        String[] contentKeyValue;

        Map<String, String> titleMap = new HashMap<>();
        Map<String, String> contentMap = new HashMap<>();
        List<Map<String, String>> mappedList = new ArrayList<>();

        System.out.println("readRemainJsonStream()");

        while ((readCount = inputStream.read(readBuffer)) != -1) {
            System.out.println("readCount: " + readCount);
            //readBuffer[readCount] = '\0';
            // TODO:
            // 실제로 readLine에서 CRLF 나오는 라인 찾아서 그 아래만 처리하고 빠져나와도 무방한 상태임
            // form으로 전달되는 post data 처리

            String convertedString = new String(readBuffer);
            System.out.print(convertedString);

            final String[] findJsonTarget = convertedString.split("\r\n");

            /*
            for (String target: findJsonTarget) {
                System.out.println("line: " + target.trim());
            }
             */

            final String[] separatedParameters = findJsonTarget[findJsonTarget.length - 1].split("&");

            /*
            for (String parameter: separatedParameters) {
                System.out.println("parameter: " + parameter);
            }
             */

            titleKeyValue = separatedParameters[0].split("=");
            contentKeyValue = separatedParameters[1].split("=");

            System.out.println();

            System.out.println("title 처리 결과- " + titleKeyValue[0] + ": " + titleKeyValue[1]);
            System.out.println("content 처리 결과 - " + contentKeyValue[0] + ": " + contentKeyValue[1].trim());

            titleMap.put(titleKeyValue[0], titleKeyValue[1]);
            contentMap.put(contentKeyValue[0], contentKeyValue[1].trim());

            mappedList.add(titleMap);
            mappedList.add(contentMap);

            jsonMappedDataList.add(mappedList);

            break;
        }
    }

    private static byte[] replaceMustacheData(String targetString) {

        int idx = 0;
        List<Integer> startedIdx = new ArrayList<>();
        List<Integer> finalIdx = new ArrayList<>();

        System.out.println("mustache: " + targetString.indexOf("{{"));

        while ((idx = targetString.indexOf("{{", idx)) != -1) {
            startedIdx.add(idx);
            idx++;
        }

        while ((idx = targetString.indexOf("}}", idx)) != -1) {
            finalIdx.add(idx);
            idx++;
        }

        for (int i = 0; i < startedIdx.size(); i++) {
            System.out.println("start: " + startedIdx.get(i));
            System.out.println("end: " + finalIdx.get(i));
        }

        List<String> mustacheString = new ArrayList<>();

        for (int i = 0; i < startedIdx.size(); i++) {
            mustacheString.add(targetString.substring(startedIdx.get(i) + 2, finalIdx.get(i)));
            System.out.println("result: " + mustacheString.get(i));
        }

        System.out.println("mustache 문자열 맵핑");

        List<Map<String, String>> innerList = new ArrayList<>();
        Map<String, String> tmpMap = new HashMap<>();

        for (int i = 0; i < jsonMappedDataList.size(); i++) {
            innerList = jsonMappedDataList.get(i);

            for (int j = 0; j < innerList.size(); j++) {
                tmpMap = innerList.get(j);

                for (Map.Entry<String, String> entrySet : tmpMap.entrySet()) {
                    System.out.println(entrySet.getKey() + ": " + entrySet.getValue());
                }
            }
        }

        List<Integer> matchingIdx = new ArrayList<>();
        List<String> mappedKey = new ArrayList<>();
        List<String> mappedData = new ArrayList<>();

        for (int j = 0; j < innerList.size(); j++) {
            tmpMap = innerList.get(j);

            for (Map.Entry<String, String> entrySet : tmpMap.entrySet()) {
                for (int i = 0; i < startedIdx.size(); i++) {
                    String tmpKey = entrySet.getKey();
                    String tmpValue = entrySet.getValue();
                    System.out.println("tmpKey: " + tmpKey + ", tmpValue: " + tmpValue);

                    if (mustacheString.get(i).equals(tmpKey)) {
                        matchingIdx.add(i);
                        mappedKey.add(tmpKey);
                        mappedData.add(tmpValue);
                    }
                }
            }
        }

        System.out.println("matchingIdx: " + matchingIdx.size());

        String completedString = targetString;
        int replaceStringLength;
        int keyStringLength;
        int difference = 0;

        final int START_BRACE = 2;
        final int END_BRACE = 2;

        for (int i = 0; i < matchingIdx.size(); i++) {
            Integer start = startedIdx.get(matchingIdx.get(i));
            Integer end = finalIdx.get(matchingIdx.get(i));

            String replaceString = mappedData.get(i);
            String keyString = mappedKey.get(i);

            String startString = completedString.substring(0, start + difference - START_BRACE * i);
            String remainString = completedString.substring(end + 2 + difference - 1 * i, completedString.length());
            //System.out.println("startString: " + startString);
            //System.out.println("remainString: " + remainString);
            replaceStringLength = replaceString.length();
            keyStringLength = keyString.length();
            difference = keyStringLength - replaceStringLength - (START_BRACE + END_BRACE) * (i + 1);

            System.out.println("difference: " + difference);

            System.out.println("result: ");
            completedString = startString + replaceString + remainString;
            System.out.println(completedString);
        }

        return completedString.getBytes();
    }

}