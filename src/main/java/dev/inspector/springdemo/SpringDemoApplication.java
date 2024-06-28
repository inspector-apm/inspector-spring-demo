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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@EnableScheduling
//@EnableJms
@ComponentScan(basePackages = {"dev.inspector.springdemo", "dev.inspector.spring"})
public class SpringDemoApplication /*implements WebMvcConfigurer*/ {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }



}
