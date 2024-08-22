package dev.inspector.springdemo.controller;

import dev.inspector.springdemo.dto.UserDto;
import dev.inspector.springdemo.entity.User;
import dev.inspector.springdemo.service.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class TestController {

    @Autowired
    private TestServiceImpl testServiceImpl;

    @GetMapping("/hello/{name}")
    User test(@PathVariable String name) {
        try {
            System.out.println("REST request received.");
            //Integer num = Integer.parseInt(name);
            User user = testServiceImpl.findUser(name);
            return user;
        } catch (Exception e) {
            String message = "Error: " + e.getMessage();
            e.printStackTrace();
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            stackTraceElements[0].getFileName();
            stackTraceElements[0].getLineNumber();
            //System.out.println(message);
            return null;
        }
    }

    @PostMapping("/hello/create")
    User create() {
        System.out.println("REST request received.");
        User user = new User();
        user.setId(1L);
        user.setName("alex");
        return testServiceImpl.saveUser(user);
    }


    @PostMapping("/hello")
    void test(@RequestBody UserDto user) {
        System.out.println("REST request received.");
        System.out.println("User: " + user.getNome());
    }

    @GetMapping("")
    ResponseEntity<String> test() {
       return ResponseEntity.ok("Hello");
    }
}
