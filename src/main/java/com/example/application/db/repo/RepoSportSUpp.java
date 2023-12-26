package com.example.application.db.repo;

import com.example.application.db.model.SportSupp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RepoSportSUpp extends MongoRepository<SportSupp, String> {
    List<SportSupp> findSportSuppBy();

}

