package example;

import com.asouwn.vertxAnno.AnnotationHandler;
import com.asouwn.vertxAnno.Application;
import com.asouwn.vertxAnno.ServeScan;
import com.asouwn.vertxAnno.serve.Port;
import com.asouwn.vertxAnno.vertical.VerticalNum;

import java.io.IOException;


@ServeScan("example.controller")
@Application
@VerticalNum(3)
@Port(8082)
public class ExampleApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        AnnotationHandler.run(ExampleApplication.class);
    }
}
