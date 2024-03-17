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
//    @Autowired
//    private JmsTemplate jmsTemplate;
//    @Autowired
//    private MessagePushInterceptor messagePushInterceptor;

    public User findUser(String name) {
        executeOutgoingRESTRequest();
//        testMessagePush("Test message");
        User user = testRepository.findByName(name);
        return user;
    }

    private void executeOutgoingRESTRequest() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://httpbin.org/get", String.class);
    }

//    public void testMessagePush(String message) {
//        jmsTemplate.convertAndSend("anotherQueue", message, messagePushInterceptor);
//    }

}
