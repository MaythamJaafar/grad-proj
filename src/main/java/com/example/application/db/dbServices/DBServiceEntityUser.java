package com.example.application.db.dbServices;

import com.example.application.db.model.User;
import com.example.application.db.repo.RepoUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBServiceEntityUser {
    private final RepoUser repoUser;

    public DBServiceEntityUser(RepoUser repoUser) {
        this.repoUser = repoUser;
    }

    public void saveUser(User user) {
        repoUser.save(user);
    }

    public List<User> findAllUser(){
        return repoUser.findAll();
    }

    public User findUserByUsername(String username){
        return repoUser.findByUsername(username);
    }
    public void deleteById(String id){
        repoUser.deleteById(id);
    }

}