package dev.inspector.springdemo.controller;

import dev.inspector.springdemo.dto.UserDto;
import dev.inspector.springdemo.entity.User;
import dev.inspector.springdemo.service.TestServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestServiceImpl testServiceImpl;

    @GetMapping("/hello/{name}")
    User test(@PathVariable String name) {
        try {
            LOGGER.info("Request received for name {}", name);
            User user = testServiceImpl.findUser(name);
            return user;
        } catch (Exception e) {
            LOGGER.error("Exception occurred", e);
            return null;
        }
    }

    @GetMapping("/dbAndOutgoingHttp/hello/{name}")
    User testDBAndHttpRequestMonitoring(@PathVariable String name) {
        try {
            LOGGER.info("Request received for name {}", name);
            User user = testServiceImpl.findUserAndSendHTTPRequest(name);
            return user;
        } catch (Exception e) {
            LOGGER.error("Exception occurred", e);
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
