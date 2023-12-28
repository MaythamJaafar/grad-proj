package com.example.application.db.dbServices;

import com.example.application.db.model.EyeSupp;
import com.example.application.db.repo.RepoEyeSupp;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DBServicesEyeSupp {
    private final RepoEyeSupp repoEyeSupp;

    public DBServicesEyeSupp(RepoEyeSupp repoEyeSupp) {
        this.repoEyeSupp = repoEyeSupp;
    }

    public void saveEyeSupp(EyeSupp newEyeSupp){
        repoEyeSupp.save(newEyeSupp);
    }

    public List<EyeSupp> findAllEyeSupp(){
        return repoEyeSupp.findAll();
    }
    public void deleteById(String id){
        repoEyeSupp.deleteById(id);
    }
}
