package com.example.application.db.dbServices;

import com.example.application.db.model.SportSupp;
import com.example.application.db.repo.RepoSportSUpp;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBServiceSportSupp {
    private final RepoSportSUpp repoSportSupp;

    public DBServiceSportSupp(RepoSportSUpp repoSportSupp) {
        this.repoSportSupp = repoSportSupp;
    }

    public void saveSportSupp(SportSupp newSportSupp){
        repoSportSupp.save(newSportSupp);
    }

    public List<SportSupp> findAllSportSUpp(){
        return repoSportSupp.findAll();
    }
    public void deleteById(String id){
        repoSportSupp.deleteById(id);
    }



}