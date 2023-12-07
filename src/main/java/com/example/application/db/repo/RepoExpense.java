package com.example.application.db.repo;

import com.example.application.db.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepoExpense extends MongoRepository<Expense, String> {

}
