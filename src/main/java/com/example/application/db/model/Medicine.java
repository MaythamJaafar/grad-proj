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
public class Medicine {
    private String _id;
    private String name;
    private String category;
    private String BatchNo;
    private String dosage;
    private String quantity;
    private LocalDate expiryDate;
    private String buyingPrice;
    private String sellingPrice;
    private String details;
    private String formula;


}
