package com.example.application.db.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class Pharmacy {
    private String _id;
    private List<Medicine> medicineList;
    private List<User> userList;

}
