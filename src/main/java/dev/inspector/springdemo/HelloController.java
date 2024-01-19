package dev.inspector.springdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class HelloController {


    @GetMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }


}
