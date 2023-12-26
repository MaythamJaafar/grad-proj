package com.example.application.db.dbServices;

import com.example.application.db.model.PCare;
import com.example.application.db.repo.RepoPCare;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBServicePCare {
    private final RepoPCare repoPCare;

    public DBServicePCare(RepoPCare repoPCare) {
        this.repoPCare = repoPCare;
    }

    public void savePCare(PCare newPCare){
        repoPCare.save(newPCare);
    }

    public List<PCare> findAllPCare(){
        return repoPCare.findAll();
    }

    public void deleteById(String id){
        repoPCare.deleteById(id);
    }



}