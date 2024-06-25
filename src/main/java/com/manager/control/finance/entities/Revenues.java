package com.manager.control.finance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "revenues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Revenues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "receiving_date")
    private LocalDate receivingDate;

    @ManyToOne
    private Category category;
}
