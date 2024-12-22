package dev.inspector.springdemo.service;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.springdemo.entity.User;
import dev.inspector.springdemo.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private RestTemplate restTemplate;

    public User findUser(String name) {
        User user = testRepository.findByName(name);
        return user;
    }

    public void sendHTTPRequest() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://httpbin.org/get", String.class);
    }

    public User findUserAndSendHTTPRequest(String name) {
        sendHTTPRequest();
        return findUser(name);
    }

    public User saveUser(User user) {
        return testRepository.save(user);
    }

}
