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
public class Expense {
    private String _id;
    private String title;
    private String details;
    private String price;
    private LocalDateTime date;

}
