package com.example.application.security;

import com.example.application.db.model.User;
import com.example.application.db.repo.RepoUser;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticatedUser {

    private final RepoUser repoUser;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(RepoUser repoUser, AuthenticationContext authenticationContext) {
        this.repoUser = repoUser;
        this.authenticationContext = authenticationContext;
    }

    public Optional<User> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> repoUser.findByUsername(userDetails.getUsername()));
    }

    public void logout() {
        authenticationContext.logout();
    }

}