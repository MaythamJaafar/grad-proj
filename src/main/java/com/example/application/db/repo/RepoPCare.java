package com.example.application.db.repo;

import com.example.application.db.model.PCare;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepoPCare extends MongoRepository<PCare, String> {



}

