package com.eddicorp.network;

import com.eddicorp.blog.controller.BlogPostController;
import com.eddicorp.controller.IndexController;
import com.eddicorp.controller.LogoutController;
import com.eddicorp.controller.SignInController;
import com.eddicorp.controller.SignUpController;
import com.eddicorp.http.HttpMethod;
import com.eddicorp.http.HttpRequest;
import com.eddicorp.http.HttpResponse;

import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MainRouter implements Controller {

    private static final String STATIC_FILE_PATH = "pages";
    private static final Map<RequestMapper, Controller> requestMap = new HashMap<>();

    static {
        final RequestMapper mapIndexPage = new RequestMapper("/", HttpMethod.GET);
        requestMap.put(mapIndexPage, new IndexController());

        final RequestMapper mapSignUp = new RequestMapper("/users", HttpMethod.POST);
        requestMap.put(mapSignUp, new SignUpController());

        final RequestMapper mapLogin = new RequestMapper("/login", HttpMethod.POST);
        requestMap.put(mapLogin, new SignInController());

        final RequestMapper mapLogout = new RequestMapper("/logout", HttpMethod.GET);
        requestMap.put(mapLogout, new LogoutController());

        final RequestMapper mapWritePost = new RequestMapper("/post", HttpMethod.POST);
        requestMap.put(mapWritePost, new BlogPostController());
    }

    private final Controller staticFileController = new StaticFileController();
    private final Controller notFoundController = new NotFoundController();

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        final String uri = request.getUri();
        final RequestMapper requestMapper = new RequestMapper(uri, request.getHttpMethod());
        final Controller maybeController = requestMap.get(requestMapper);
        System.out.println("maybeController = " + maybeController);
        if (maybeController != null) {
            maybeController.handle(request, response);
            return;
        }

        final String pathToLoad = Paths.get(STATIC_FILE_PATH, uri).toString();
        System.out.println("pathToLoad = " + pathToLoad);
        final URL maybeResource = this.getClass().getClassLoader().getResource(pathToLoad);
        System.out.println("maybeResource = " + maybeResource);
        if (maybeResource != null) {
            staticFileController.handle(request, response);
            return;
        }

        notFoundController.handle(request, response);
    }
}
