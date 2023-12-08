package com.example.application.db.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String _id;
    private String fullName;
    private String phoneNb;
    private Gender gender;
    private Role role;
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private LocalDate joinedAt;

    public enum Role {
        ADMIN,
        CLIENT,
        SUPER_ADMIN
    }
    public enum Gender {
        MALE,
        FEMALE
    }

}
