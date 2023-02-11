package com.eddicorp.decode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;

@DisplayName("Mustache test")
public class UrlDecodeTest {

    @Test
    void URLDecode_테스트() {

        String Data = "투케이,28,man";
        System.out.println("원본 : "+Data);

        String encodeData = "";
        String decodeData = "";

        //==== url 인코딩 수행 실시 ====
        try {
            encodeData = URLEncoder.encode(Data, "UTF-8");
            System.out.println("URL 인코딩 : "+encodeData);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        //==== url 디코딩 수행 실시 ====
        try {
            decodeData = URLDecoder.decode(encodeData, "UTF-8");
            System.out.println("URL 디코딩 : "+decodeData);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void URLDecode_영문_테스트() {

        String Data = "english";
        System.out.println("원본 : "+Data);

        String encodeData = "";
        String decodeData = "";

        //==== url 인코딩 수행 실시 ====
        try {
            encodeData = URLEncoder.encode(Data, "UTF-8");
            System.out.println("URL 인코딩 : "+encodeData);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        //==== url 디코딩 수행 실시 ====
        try {
            decodeData = URLDecoder.decode(encodeData, "UTF-8");
            System.out.println("URL 디코딩 : "+decodeData);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
