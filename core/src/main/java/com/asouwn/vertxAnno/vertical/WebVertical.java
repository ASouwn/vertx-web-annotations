package com.asouwn.vertxAnno.vertical;

import com.asouwn.vertxAnno.config.CorsHandlerFactory;
import com.asouwn.vertxAnno.serve.GetMapping;
import com.asouwn.vertxAnno.serve.PostMapping;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebVertical extends AbstractVerticle {

    private final List<Class<?>> classes;

    private final Map<String, Object> instanceMap = new HashMap<>();
    private int port = 8080;

    public WebVertical(List<Class<?>> classes, int port) {
        this.classes = classes;
        this.port = port;
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        HttpServer server = vertx.createHttpServer();
        router.route().handler(BodyHandler.create());
        initInstanceMap(classes);
        for (Class<?> c : classes) {
            createSingleServe(c, router);
        }
        server.requestHandler(router).listen(port, asyncResult ->
        {
            if (asyncResult.succeeded()) {
                System.out.println("start serve on port " + port + " on thread "+ Thread.currentThread().getName());
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
                        return Future.succeededFuture(method.invoke(instanceMap.get(c.getName())));
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (method.isAnnotationPresent(PostMapping.class)) {
                PostMapping post = method.getAnnotation(PostMapping.class);
                router.post("/" + post.value()).respond(ctx -> {
                    Object[] params = {ctx.body().asJsonObject().mapTo(method.getParameterTypes()[0])};
                    try {
                        return Future.succeededFuture(method.invoke(instanceMap.get(c.getName()), params));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    private void initInstanceMap(List<Class<?>> classes) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Class<?> c : classes) {
            instanceMap.put(c.getName(), c.getDeclaredConstructor().newInstance());
        }
    }
}
