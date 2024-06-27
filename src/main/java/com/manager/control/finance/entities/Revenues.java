package com.manager.control.finance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    private BigDecimal amount;
    @Column(name = "receiving_date")
    private LocalDate receivingDate;
    @Column(name = "update_job")
    private Boolean updateJob = false;

    @ManyToOne
    private Category category;
}
