package com.example.application.db.repo;

import com.example.application.db.model.Medicine;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RepoMedicine extends MongoRepository<Medicine, String> {
    List<Medicine> findAllByCategory(String category);

}
