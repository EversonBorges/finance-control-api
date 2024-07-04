package com.manager.control.finance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "budgeted")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Budgeted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "register_date")
    private LocalDate registerDate;
    @Column(name = "reference_year")
    private int referenceYear;
    @Column(name = "reference_month")
    private int referenceMonth;
    @Column(name = "budgeted_amount")
    private BigDecimal budgetedAmount;

    @ManyToOne
    private Category category;
}
