package com.example.application.db.repo;

import com.example.application.db.model.Baby;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RepoBaby extends MongoRepository<Baby, String> {
}
