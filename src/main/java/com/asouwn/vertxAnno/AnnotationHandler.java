package com.asouwn.vertxAnno;

import com.asouwn.vertxAnno.Serve.GetMapping;
import com.asouwn.vertxAnno.Serve.PostMapping;
import com.asouwn.vertxAnno.Serve.RestController;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;


public class AnnotationHandler {
    /**
     * serve scan direction
     */
    private static String serveScanDir;
    /**
     * serve start port
     */
    private static int port = 8080;

    /**
     * the start state
     *
     * @param c the application.class
     * @param s the param should do, but there do nothing even have action
     * @throws IOException
     */
    public static void run(Class<?> c, String[] s) throws IOException, ClassNotFoundException {
        if (!c.isAnnotationPresent(Application.class)) {
            throw new RuntimeException("there's no annotation @Application on main Class");
        }
        serveScanDir = c.getAnnotation(ServeScan.class).value();
        serveStart(serveScanDir);
    }

    /**
     * get the handler URL which ServeScan annotating
     *
     * @param serveScanDir the annotation @ServeScan mentioned
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void serveStart(String serveScanDir) throws IOException, ClassNotFoundException {
        List<File> resources = getResources(serveScanDir);
        List<Class<?>> classes = findClasses(resources);
        createHttpServe(classes);
    }

    /**
     * get the file list under the scanned dir;
     *
     * @param serveScanDir
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<File> getResources(String serveScanDir) throws IOException, ClassNotFoundException {
        List<File> files = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = loader.getResources(serveScanDir.replace(".", "/"));
        File scanFrom = new File(resources.nextElement().getFile());
        Collections.addAll(files, Objects.requireNonNull(scanFrom.listFiles()));
        return files;
    }

    /**
     * get the controller classes by file name;
     *
     * @return the controller classes
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> findClasses(List<File> files) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        assert files != null;
        for (File file : files) {
            if (file.isFile()) {
                Class<?> c = Class.forName(serveScanDir + "." + file.getName().substring(0, file.getName().length() - 6));
                if (c.isAnnotationPresent(RestController.class)) {
                    classes.add(c);
                }
            }
        }
        return classes;
    }

    private static void createHttpServe(List<Class<?>> classes) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        for (Class<?> c : classes) {
            createSingleServe(c, router);
        }

        server.requestHandler(router).listen(port, asyncResult -> {
            if (asyncResult.succeeded()) {
                System.out.println("start serve on port " + port);
            } else {
                System.out.println("start serve error, something got wrong");
            }
        });
    }

    /**
     * create a serve from a class instance
     *
     * @param c
     */
    private static void createSingleServe(Class<?> c, Router router) {
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
