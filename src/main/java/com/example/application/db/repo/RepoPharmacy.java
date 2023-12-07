package com.example.application.db.repo;

import com.example.application.db.model.Pharmacy;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepoPharmacy extends MongoRepository<Pharmacy, String> {

}
