package dev.inspector.springdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlacklistController {

    @GetMapping("/blacklist")
    String testBlacklist() {
        return "SUCCESS";
    }

    @GetMapping("/blacklistV1")
    String testBlacklistV1() {
        return "SUCCESS";
    }
}
