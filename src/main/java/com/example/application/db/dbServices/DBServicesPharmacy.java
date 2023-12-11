package com.example.application.db.dbServices;

import com.example.application.db.repo.RepoPharmacy;
import org.springframework.stereotype.Service;

@Service
public class DBServicesPharmacy {

    private final RepoPharmacy repoPharmacy;
    private final DBServiceEntityUser dbServiceEntityUser;

    public DBServicesPharmacy(RepoPharmacy repoPharmacy, DBServiceEntityUser dbServiceEntityUser) {
        this.repoPharmacy = repoPharmacy;
        this.dbServiceEntityUser = dbServiceEntityUser;
    }
}
