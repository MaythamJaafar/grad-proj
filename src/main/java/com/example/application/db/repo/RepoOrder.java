package com.example.application.db.repo;

import com.example.application.db.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepoOrder extends MongoRepository<Order, String> {

}