package dev.inspector.springdemo.controller;

import dev.inspector.springdemo.dto.UserDto;
import dev.inspector.springdemo.entity.User;
import dev.inspector.springdemo.service.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
