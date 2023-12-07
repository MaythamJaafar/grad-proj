package com.example.application.db.repo;

import com.example.application.db.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepoUser extends MongoRepository<User, String> {
    User findByUsername(String username);
}
