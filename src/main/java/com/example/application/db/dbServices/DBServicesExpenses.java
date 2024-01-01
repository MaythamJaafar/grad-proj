package com.example.application.db.dbServices;

import com.example.application.db.model.Expense;
import com.example.application.db.repo.RepoExpense;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBServicesExpenses {

    private final RepoExpense repoExpense;

    public DBServicesExpenses(RepoExpense repoExpense) {
        this.repoExpense = repoExpense;
    }
    public List<Expense> findAllExpenses(){
        return repoExpense.findAll();
    }
    public void saveExpense(Expense expense){
        repoExpense.save(expense);
    }
    public void deletExpense(String _id){
        repoExpense.deleteById(_id);
    }

}
