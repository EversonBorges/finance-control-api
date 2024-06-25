package com.manager.control.finance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "expenses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String establishment;
    @Column(name = "number_installment")
    private Integer numberInstallment;
    @Column(name = "quantity_installments")
    private Integer quantityInstallments;
    @Column(name = "values_installment")
    private Double valuesInstallment;
    private String week;
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;
    @Column(name = "reference_month")
    private Integer referenceMonth;
    @Column(name = "reference_year")
    private Integer referenceYear;

    @ManyToOne
    private Category category;

    @ManyToOne
    private PaymentMethods paymentMethods;

    @ManyToOne
    private CreditCard creditCard;

    public Expenses(String establishment, Integer numberInstallment, Integer quantityInstallments,
                    Double valuesInstallment, LocalDate purchaseDate,Category category, PaymentMethods paymentMethods) {
        this.establishment = establishment;
        this.numberInstallment = numberInstallment;
        this.quantityInstallments = quantityInstallments;
        this.valuesInstallment = valuesInstallment;
        this.purchaseDate = purchaseDate;
        this.category = category;
        this.paymentMethods = paymentMethods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expenses expenses = (Expenses) o;
        return Objects.equals(id, expenses.id) && Objects.equals(establishment, expenses.establishment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, establishment);
    }
}
