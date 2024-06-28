package dev.inspector.springdemo.scheduler;

import dev.inspector.springdemo.entity.User;
import dev.inspector.springdemo.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestSchedulerDue {

    @Autowired
    TestRepository testRepository;

    @Scheduled(fixedRate = 100000)
    public void runTaskDue() {
        System.out.println("Running scheduled task due...");
        User user = new User(3L, "PinottoDue");
        testRepository.save(user);
    }
}
