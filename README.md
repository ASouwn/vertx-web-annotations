# Vertx Web Annotations
注解驱动的vertx web开发框架，实现类似于spring的开发手感，底层目前是以反射机制+单例实现的，并且支持json，如controller中方法返回为自定义类，则以json类型返回，目前没有完善，未在maven中央仓库中

使用事例
~~~java
Main class

@ServeScan("the reference path of the controller package")
@Application
@VerticalNum(3)
@Port(8082)
public class ExampleApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        AnnotationHandler.run(ExampleApplication.class);
    }
}

controller package
controller class

@RestController
public class HelloController {
    @GetMapping("hello")
    public String helloServe() {
        return "hello world";
    }
~~~

