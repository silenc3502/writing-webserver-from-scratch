package com.eddicorp.router.router;

import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;
import com.eddicorp.router.MainRouter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("MainRouter test")
public class RouterTest {

    @Test
    public void 메인_페이지_요청() throws IOException {

        final String requestMessage =
                "GET / HTTP/1.1\r\n"  +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: " + 0 + "\r\n" +
                "\r\n";

        final String responseString = "HTTP/1.1 200 OK\n" +
                "Content-Length: 657\n" +
                "Content-Type: text/html;charset=utf-8\n" +
                "\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Simple Blog</title>\n" +
                "    <link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"assets/css/main.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<header id=\"main-nav\">\n" +
                "    <a class=\"main-logo\" href=\"/\">블로그</a>\n" +
                "    <ul>\n" +
                "        <li><a href=\"/\">글 목록</a></li>\n" +
                "        <li><a href=\"write-post.html\">작성하기</a></li>\n" +
                "    </ul>\n" +
                "    <a class=\"main-account-setting\" href=\"login.html\">\n" +
                "        <img src=\"assets/icon/manage_accounts_white_24dp.svg\">\n" +
                "    </a>\n" +
                "</header>\n" +
                "<article id=\"main-post\">\n" +
                "</article>\n" +
                "</body>\n" +
                "</html>";

        final byte[] requestBytes = requestMessage.getBytes(StandardCharsets.UTF_8);
        final InputStream byteArrayInputStream = new ByteArrayInputStream(requestBytes);
        final HttpRequest httpRequest = new HttpRequest(byteArrayInputStream);

        final File fileToExport = new File("./src/main/resources/cantMake/response.txt");

        OutputStream outputStream = new FileOutputStream(fileToExport);
        HttpResponse httpResponse = new HttpResponse(outputStream);

        MainRouter mainRouter = new MainRouter();
        mainRouter.handle(httpRequest, httpResponse);

        File file = new File("./src/main/resources/cantMake/response.txt");
        Charset charset = StandardCharsets.UTF_8;

        InputStream in = new FileInputStream(file);

        byte[] bytes = new byte[(int) file.length()];
        in.read(bytes);

        String content = new String(bytes, charset);
        //System.out.print(content);
        //System.out.println("file.encoding="+System.getProperty("file.encoding"));

        //String UTF8String = URLEncoder.encode(responseString, "UTF-8");
        String testS = "test";

        //assertEquals("test", testS);
        assertEquals(responseString.trim().replace("\r",""),
                content.trim().replace("\r",""));
        //assertThat(responseString).isEqualToNormalizingNewlines(content);
    }

    @Test
    public void 회원가입_요청() throws IOException {

        final String formData = "username=test&password=test";
        final int formLength = formData.length();

        final String requestMessage =
                "POST /users HTTP/1.1\r\n"  +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: " + formLength + "\r\n" +
                "\r\n" +
                formData;

        final String responseString = "HTTP/1.1 302 Found\n" +
                "Location: /login.html\n";

        final byte[] requestBytes = requestMessage.getBytes(StandardCharsets.UTF_8);
        final InputStream byteArrayInputStream = new ByteArrayInputStream(requestBytes);
        final HttpRequest httpRequest = new HttpRequest(byteArrayInputStream);

        final File fileToExport = new File("./src/main/resources/cantMake/response.txt");

        OutputStream outputStream = new FileOutputStream(fileToExport);
        HttpResponse httpResponse = new HttpResponse(outputStream);

        MainRouter mainRouter = new MainRouter();
        mainRouter.handle(httpRequest, httpResponse);

        File file = new File("./src/main/resources/cantMake/response.txt");
        Charset charset = StandardCharsets.UTF_8;

        InputStream in = new FileInputStream(file);

        byte[] bytes = new byte[(int) file.length()];
        in.read(bytes);

        String content = new String(bytes, charset);

        assertEquals(responseString.trim().replace("\r",""),
                content.trim().replace("\r",""));
    }
}
