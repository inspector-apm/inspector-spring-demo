package dev.inspector.springdemo.repository;

import dev.inspector.springdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TestRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}
