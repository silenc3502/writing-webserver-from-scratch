package com.eddicorp.mustache.operation;

import com.eddicorp.blog.Post;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DisplayName("Mustache test")
public class BasicTest {

    @Test
    void mustache_렌더링_테스트() {

        final Mustache.Compiler compiler = Mustache.compiler();
        final String templateStringFromIndexHtmlFile = "<article id=\"mainpost\">\n" +
                " {{#posts}}\n" +
                " <div class=\"post-box\">\n" +
                " <h1>{{title}}</h1>\n" +
                " <h3>작성자: {{author}}</h3>\n" +
                " <p>\n" +
                " {{content}}\n" +
                " </p>\n" +
                " </div>\n" +
                " {{/posts}}\n" +
                "</article>";

        final List<Post> posts = new ArrayList<Post>();
        posts.add(new Post(1L, "hi", "hi", "hi"));
        posts.add(new Post(2L, "test", "test", "test"));

        final Template template =
                compiler.compile(templateStringFromIndexHtmlFile);

        final Map<String, Object> context = new HashMap<>();
        context.put("posts", posts);
        final String renderedPage = template.execute(context);

        System.out.println(renderedPage);
    }

    @Test
    void mustache_일반_파일_렌더링_테스트() {

        final Mustache.Compiler compiler = Mustache.compiler();
        final String templateStringFromIndexHtmlFile = "<article id=\"mainpost\">\n" +
                " <div class=\"post-box\">\n" +
                " <h1>hi</h1>\n" +
                " <h3>작성자: hi</h3>\n" +
                " <p>\n" +
                "    hi\n" +
                " </p>\n" +
                " </div>\n" +
                " <div class=\"post-box\">\n" +
                " <h1>test</h1>\n" +
                " <h3>작성자: test</h3>\n" +
                " <p>\n" +
                "    test\n" +
                " </p>\n" +
                " </div>\n" +
                "</article>";

        final List<Post> posts = new ArrayList<Post>();
        posts.add(new Post(1L, "hi", "hi", "hi"));
        posts.add(new Post(2L, "test", "test", "test"));

        final Template template =
                compiler.compile(templateStringFromIndexHtmlFile);

        final Map<String, Object> context = new HashMap<>();
        context.put("posts", posts);
        final String renderedPage = template.execute(context);

        System.out.println(renderedPage);
    }

    @Test
    void mustache_로그인_테스트() {

        final Mustache.Compiler compiler = Mustache.compiler();
        final String templateStringFromIndexHtmlFile = "<header id=\"main-nav\">\n" +
                "    <a class=\"main-logo\" href=\"/\">블로그</a>\n" +
                "    <ul>\n" +
                "        <li><a href=\"/\">글 목록</a></li>\n" +
                "        <li><a href=\"write-post.html\">작성하기</a></li>\n" +
                "    </ul>\n" +
                "    {{#isLoggedIn}}\n" +
                "    <a class=\"main-account-setting\" href=\"/logout\">\n" +
                "        <img src=\"assets/icon/logout.svg\">\n" +
                "    </a>\n" +
                "    {{/isLoggedIn}}\n" +
                "    {{^isLoggedIn}}\n" +
                "    <a class=\"main-account-setting\" href=\"login.html\">\n" +
                "        <img src=\"assets/icon/manage_accounts_white_24dp.svg\">\n" +
                "    </a>\n" +
                "    {{/isLoggedIn}}\n" +
                "</header>";

        //final List<Boolean> isLoggedIn = new ArrayList();
        //isLoggedIn.add(true);

        final Template template =
                compiler.compile(templateStringFromIndexHtmlFile);

        final Map<String, Object> context = new HashMap<>();
        context.put("isLoggedIn", false);
        final String renderedPage = template.execute(context);

        System.out.println(renderedPage);
    }

}
