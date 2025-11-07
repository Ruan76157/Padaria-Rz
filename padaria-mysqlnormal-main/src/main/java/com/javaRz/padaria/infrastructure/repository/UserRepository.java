package com.javaRz.padaria.infrastructure.repository;

import com.javaRz.padaria.infrastructure.entitys.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
