package com.asouwn.vertxAnno;

import com.asouwn.vertxAnno.serve.Port;
import com.asouwn.vertxAnno.vertical.VerticalNum;
import com.asouwn.vertxAnno.vertical.WebVertical;
import com.asouwn.vertxAnno.serve.RestController;
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
    private static int port = 8080;
    private static int verticalNum = 4;

    /**
     * the start state
     *
     * @param c the application.class
     * @throws IOException
     */
    public static void run(Class<?> c) throws IOException, ClassNotFoundException {
//        test if the application starter class
        if (!c.isAnnotationPresent(Application.class)) {
            throw new RuntimeException("there's no annotation @Application on main Class");
        }
        serveScanDir = c.getAnnotation(ServeScan.class).value();
//        test if there an annotation about vertical config
        if (c.isAnnotationPresent(VerticalNum.class)){
            verticalNum = c.getAnnotation(VerticalNum.class).value();
        } else {
            System.out.println("use the default num of vertical, you can change by use @VerticalNum(<num>) on application class");
        }
        if (c.isAnnotationPresent(Port.class)){
            port = c.getAnnotation(Port.class).value();
        } else {
            System.out.println("use the default port 8080, you can change by use @Port(<num>) on application class");
        }
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
        WebVertical vertical = webVertical(classes);
        for (int i = 0; i < verticalNum; i++) {
            vertx.deployVerticle(vertical);
        }
    }

    /**
     * create a serve from a class instance
     *
     * @param c
     */

    private static WebVertical webVertical(List<Class<?>> c){
        return new WebVertical(c, port);
    }

}
