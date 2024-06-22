package example.controller;

import com.asouwn.vertxAnno.serveAnnotation.GetMapping;
import com.asouwn.vertxAnno.serveAnnotation.PostMapping;
import com.asouwn.vertxAnno.serveAnnotation.RequestMapping;
import com.asouwn.vertxAnno.serveAnnotation.RestController;
import com.asouwn.vertxAnno.serveAnnotation.param.RequestBody;
import example.param.HelloRequest;
import example.param.HelloResponse;

@RestController
//@RequestMapping("test")
public class HelloController {
    @GetMapping("hello")
    public String helloServe() {
//        return new HelloResponse("hello world");
        return "hello world";
    }

    @PostMapping("name")
    public HelloResponse getName(@RequestBody HelloRequest request) {
        return new HelloResponse(request.getName());
    }
}
