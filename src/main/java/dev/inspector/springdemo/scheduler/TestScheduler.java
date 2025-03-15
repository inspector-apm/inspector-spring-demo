package dev.inspector.springdemo.scheduler;

import dev.inspector.springdemo.entity.User;
import dev.inspector.springdemo.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TestScheduler {

    @Autowired
    TestRepository testRepository;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void runTask() {
        System.out.println("Running scheduled task...");
        User user = new User(2L, "PinottoUno");
        testRepository.save(user);
    }
}
