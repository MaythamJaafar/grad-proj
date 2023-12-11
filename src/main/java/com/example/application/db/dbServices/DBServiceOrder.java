package com.example.application.db.dbServices;

import com.example.application.db.repo.RepoOrder;
import org.springframework.stereotype.Service;

@Service
public class DBServiceOrder {
    private final RepoOrder repoOrder;

    public DBServiceOrder(RepoOrder repoOrder) {
        this.repoOrder = repoOrder;
    }
}
