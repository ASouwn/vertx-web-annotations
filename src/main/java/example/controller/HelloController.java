package example.controller;

import com.asouwn.vertxAnno.Serve.GetMapping;
import com.asouwn.vertxAnno.Serve.RequestMapping;
import com.asouwn.vertxAnno.Serve.RestController;

@RestController
@RequestMapping("test")
public class HelloController {
    @GetMapping("hello")
    public String helloServe() {
//        return new HelloResponse("hello world");
        return "hello world";
    }
}
