package example;

import com.asouwn.vertxAnno.AnnotationHandler;
import com.asouwn.vertxAnno.Application;
import com.asouwn.vertxAnno.ServeScan;

import java.io.IOException;


@ServeScan("example.controller")
@Application
public class ExampleApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        AnnotationHandler.run(ExampleApplication.class, args);
    }
}
