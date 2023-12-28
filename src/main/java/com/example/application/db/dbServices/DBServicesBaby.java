package com.example.application.db.dbServices;

import com.example.application.db.model.Baby;
import com.example.application.db.repo.RepoBaby;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class DBServicesBaby {

    private final RepoBaby repoBaby;

    public DBServicesBaby(RepoBaby repoBaby) {
        this.repoBaby = repoBaby;
    }

    public void saveBaby(Baby newBaby){
        repoBaby.save(newBaby);
    }

    public List<Baby> findAllBaby(){
        return repoBaby.findAll();
    }

    public void deleteById(String id){
        repoBaby.deleteById(id);
    }

}
