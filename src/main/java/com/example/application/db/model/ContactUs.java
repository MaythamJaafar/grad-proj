package com.example.application.db.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class ContactUs {
    private String firstName;
    private String id;
    private String lastName;
    private String email;
    private String gender;
    private String age;
    private String message;
    private String messageOwner;
    private LocalDateTime createdAt;
}
