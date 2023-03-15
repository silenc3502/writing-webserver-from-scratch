package com.eddicorp.member;

import com.eddicorp.member.repository.MemberRepository;
import com.eddicorp.member.repository.MemberRepositoryImpl;
import com.eddicorp.member.service.MemberService;
import com.eddicorp.member.service.MemberServiceImpl;
import com.eddicorp.network.RequestHandler;
import com.eddicorp.network.WebRequestReceiver;
import com.eddicorp.session.repository.SessionRepository;
import com.eddicorp.session.repository.SessionRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Member test")
public class MemberTest {

    private OutputStream outputStream;
    private InputStream inputStream;
    private RequestHandler requestHandler;

    final private MemberService memberService = new MemberServiceImpl();
    final MemberRepository memberRepository = MemberRepositoryImpl.getInstance();
    final SessionRepository sessionRepository = SessionRepositoryImpl.getInstance();

    @BeforeEach
    public void 사전준비 () throws FileNotFoundException {
        requestHandler = new RequestHandler();
        final File fileToExport = new File("./src/main/resources/cantMake/response.txt");
        outputStream = new FileOutputStream(fileToExport);
    }

    @Test
    public void 회원가입_확인 () {
        final String formData = "username=test&password=test";
        final int formLength = formData.length();

        final String requestMessage =
                "POST /users HTTP/1.1\r\n"  +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: " + formLength + "\r\n" +
                "\r\n" +
                formData;

        final byte[] requestBytes = requestMessage.getBytes(StandardCharsets.UTF_8);
        inputStream = new ByteArrayInputStream(requestBytes);
        requestHandler.handle(inputStream, outputStream);

        Member member = new Member("test", "test");
        Member sutMember = memberRepository.findByUsername("test");

        assertAll("Member 객체와 동일한가",
                () -> assertEquals(member.getUsername(), sutMember.getUsername()),
                () -> assertEquals(member.getPassword(), sutMember.getPassword())
        );
    }

    @Test
    public void 로그인_확인 () {

        memberService.signUp("test", "test");

        final String formData = "username=test&password=test";
        final int formLength = formData.length();

        final String requestMessage =
                "POST /login HTTP/1.1\r\n"  +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: " + formLength + "\r\n" +
                "\r\n" +
                formData;

        final byte[] requestBytes = requestMessage.getBytes(StandardCharsets.UTF_8);
        inputStream = new ByteArrayInputStream(requestBytes);
        requestHandler.handle(inputStream, outputStream);

        Member member = new Member("test", "test");
        Member sutMember = memberRepository.findByUsername("test");

        assertAll("Member 객체와 동일한가",
                () -> assertEquals(member.getUsername(), sutMember.getUsername()),
                () -> assertEquals(member.getPassword(), sutMember.getPassword())
        );
    }

    @Test
    public void 로그인_테스트() {
        memberService.signUp("test", "test");

        final String formData = "username=test&password=test";
        final int formLength = formData.length();

        final String requestMessage =
                "POST /login HTTP/1.1\r\n"  +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: " + formLength + "\r\n" +
                "\r\n" +
                formData;

        final byte[] requestBytes = requestMessage.getBytes(StandardCharsets.UTF_8);
        inputStream = new ByteArrayInputStream(requestBytes);
        requestHandler.handle(inputStream, outputStream);

        Member member = new Member("test", "test");
        Member sutMember = memberRepository.findByUsername("test");

        assertAll("Member 객체와 동일한가",
                () -> assertEquals(member.getUsername(), sutMember.getUsername()),
                () -> assertEquals(member.getPassword(), sutMember.getPassword())
        );
    }
}
