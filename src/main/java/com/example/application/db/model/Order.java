package com.example.application.db.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class Order {
    private String _id;
    private LocalDate orderDate;
    private double orderPrice;
    private List<Medicine> medicineList;
    private double discount;
}
