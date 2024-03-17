package dev.inspector.springdemo;

import dev.inspector.springdemo.entity.User;
import dev.inspector.springdemo.repository.TestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.apache.activemq.ActiveMQConnectionFactory;
//import org.springframework.jms.annotation.EnableJms;
//import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
//import org.springframework.jms.config.JmsListenerContainerFactory;
//import org.springframework.jms.connection.CachingConnectionFactory;
//import org.springframework.jms.core.JmsTemplate;
//import javax.jms.ConnectionFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@EnableScheduling
//@EnableJms
public class SpringDemoApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }

    // Creates a User entry at startup
    @Bean
    CommandLineRunner commandLineRunner(TestRepository testRepository) {
        return args -> {
            User user = new User(1L, "Gianni");
            testRepository.save(user);
        };
    }

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
//        connectionFactory.setBrokerURL("tcp://localhost:61616");
//        connectionFactory.setUserName("admin");
//        connectionFactory.setPassword("secret");
//        return new CachingConnectionFactory(connectionFactory);
//    }
//
//    @Bean
//    public JmsTemplate jmsTemplate() {
//        JmsTemplate jmsTemplate = new JmsTemplate();
//        jmsTemplate.setConnectionFactory(connectionFactory());
//        return jmsTemplate;
//    }
//
//    @Bean
//    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        return factory;
//    }

}