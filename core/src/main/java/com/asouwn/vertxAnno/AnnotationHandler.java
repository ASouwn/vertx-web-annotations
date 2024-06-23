package com.asouwn.vertxAnno;

import com.asouwn.vertxAnno.POJO.WebVertical;
import com.asouwn.vertxAnno.serveAnnotation.RestController;
import io.vertx.core.Vertx;

import java.io.File;
import java.io.IOException;
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
    private static final int port = 8080;

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
        for (int i = 0; i < 4; i++) {
            vertx.deployVerticle(webVertical(classes));
        }
    }

    /**
     * create a serve from a class instance
     *
     * @param c
     */

    private static WebVertical webVertical(List<Class<?>> c){
        return new WebVertical(c);
    }

}
