package dev.inspector.springdemo.controller;

import dev.inspector.springdemo.dto.UserDto;
import dev.inspector.springdemo.entity.User;
import dev.inspector.springdemo.service.TestServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/rest")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestServiceImpl testServiceImpl;

    @GetMapping("/hello/{name}")
    User testGet(@PathVariable String name) {
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
    void testPostRequest(@RequestBody UserDto user) {
        System.out.println("REST request received.");
        System.out.println("User: " + user.getNome());
    }

    @PatchMapping("/hello/patch")
    void testPatchRequest(@RequestBody UserDto user) {
        System.out.println("Patch request received.");
        System.out.println("User: " + user.getNome());
    }

    @PutMapping("/hello/put")
    void testPutRequest(@RequestBody UserDto user) {
        System.out.println("PUT request received.");
        System.out.println("User: " + user.getNome());
    }

    @GetMapping("")
    ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello");
    }


    @PostMapping("/upload")
    public ResponseEntity<String> testFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload", HttpStatus.BAD_REQUEST);
        }

        try {
            LOGGER.info("Receiving file named {} with size {} bytes", file.getName(), file.getSize());

            return ResponseEntity.ok("File processed");

        } catch (Exception e) {
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/blacklist")
    String testBlacklist() {
       return "SUCCESS";
    }

    @GetMapping("v1/blacklist")
    String testV1Blacklist() {
        return "SUCCESS";
    }
}
