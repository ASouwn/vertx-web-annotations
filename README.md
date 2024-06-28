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
# 持续开发需求
- 反射运行有点重，学长建议我用methodHandles+labmdaFactory，我目前用的方法是在扫描类的时候就生成相关实例，执行方法时就不再生成实例了，直接在kvmap中获得实例运行
- config类，预计用一个config类来管理配置问题，比如event loop, cors等等
- 数据库管理，基于vertx的event loop，也许同时也实现了数据库的并发访问，如何更好的管理数据库
- 项目结构在更新的同时也应该有较好的分类模式
