package com.example.application.db.repo;

import com.example.application.db.model.EyeSupp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
public interface RepoEyeSupp extends MongoRepository<EyeSupp, String> {

}

