package com.manager.control.finance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "investments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Investments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    @Column(name = "transaction_date")
    private LocalDate transactionDate;
    @Column(name = "reference_year")
    private int referenceYear;
    @Column(name = "reference_month")
    private int referenceMonth;
    @Column(name = "value_investments")
    private BigDecimal valueInvestments;

    @ManyToOne
    private Category category;
}
