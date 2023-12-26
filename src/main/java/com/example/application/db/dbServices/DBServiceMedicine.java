package com.example.application.db.dbServices;

import com.example.application.db.model.Medicine;
import com.example.application.db.repo.RepoMedicine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBServiceMedicine {
    private final RepoMedicine repoMedicine;

    public DBServiceMedicine(RepoMedicine repoMedicine) {
        this.repoMedicine = repoMedicine;
    }

    public void saveMedicine(Medicine newMedicine){
        repoMedicine.save(newMedicine);
    }
    public void deleteMedicine(String _id){
        repoMedicine.deleteById(_id);
    }

    public List<Medicine> findAllMedicines(){
        return repoMedicine.findAll();
    }



}
