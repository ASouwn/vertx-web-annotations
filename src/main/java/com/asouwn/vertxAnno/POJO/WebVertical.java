package com.asouwn.vertxAnno.POJO;

import com.asouwn.vertxAnno.serveAnnotation.GetMapping;
import com.asouwn.vertxAnno.serveAnnotation.PostMapping;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class WebVertical extends AbstractVerticle {

    private List<Class<?>> classes;

    public WebVertical(List<Class<?>> classes) {
        this.classes = classes;
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        HttpServer server = vertx.createHttpServer();
        for (Class<?> c: classes){
            createSingleServe(c, router);
        }
        server.requestHandler(router).listen(8080, asyncResult ->
        {
            if (asyncResult.succeeded()) {
                System.out.println("start serve on port " + 8080);
            } else {
                System.out.println("start serve error, something got wrong");
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
    private void createSingleServe(Class<?> c, Router router) {
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping get = method.getAnnotation(GetMapping.class);
                router.get("/" + get.value()).respond(ctx -> {
                    try {
                        return Future.succeededFuture(method.invoke(c.getDeclaredConstructor().newInstance()));
                    } catch (InvocationTargetException | InstantiationException | NoSuchMethodException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (method.isAnnotationPresent(PostMapping.class)) {
//                PostMapping post = method.getAnnotation(PostMapping.class);
//                router.post(post.value()).respond(ctx -> Future.succeededFuture(new Pojo()));
            }
        }
    }
}
