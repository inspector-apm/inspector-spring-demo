package dev.inspector.springdemo.controller;

import dev.inspector.springdemo.entity.User;
import dev.inspector.springdemo.service.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class TestController {

    @Autowired
    private TestServiceImpl testServiceImpl;

    @GetMapping("/hello/{name}")
    User test(@PathVariable String name) {
        System.out.println("REST request received.");
        return testServiceImpl.findUser(name);
    }
}
