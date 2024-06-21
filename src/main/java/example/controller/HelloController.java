package example.controller;

import com.asouwn.vertxAnno.serveAnnotation.GetMapping;
import com.asouwn.vertxAnno.serveAnnotation.RequestMapping;
import com.asouwn.vertxAnno.serveAnnotation.RestController;

@RestController
//@RequestMapping("test")
public class HelloController {
    @GetMapping("hello")
    public String helloServe() {
//        return new HelloResponse("hello world");
        return "hello world";
    }
}
