package com.example.application.db.repo;

import com.example.application.db.model.ContactUs;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RepoContactUs extends MongoRepository<ContactUs, String> {
    List<ContactUs> findAllByMessageOwner(String messageOwner);
}
