package com.example.application.db.dbServices;

import com.example.application.db.model.ContactUs;
import com.example.application.db.repo.RepoContactUs;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBServiceContactUs {
    private final RepoContactUs repoContactUs;
    public DBServiceContactUs(RepoContactUs repoContactUs) {
        this.repoContactUs= repoContactUs;
    }
    public void saveContact(ContactUs newContact){
        repoContactUs.save(newContact);
    }
    public List<ContactUs> findAllFeedbacks(){
        return repoContactUs.findAll();
    }
    public List<ContactUs> findAllFeedbacksByUsername(String username){
        return repoContactUs.findAllByMessageOwner(username);
    }
}
