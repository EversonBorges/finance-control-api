package com.manager.control.finance.entities;

import com.manager.control.finance.enums.PaymentMethodsEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

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
    private BigDecimal valuesInstallment;
    private String week;
    @Column(name = "transaction_date")
    private LocalDate transactionDate;
    @Column(name = "reference_month")
    private Integer referenceMonth;
    @Column(name = "reference_year")
    private Integer referenceYear;
    @Column(name = "transaction_control")
    private UUID transactionControl;
    @Enumerated(EnumType.STRING)
    private PaymentMethodsEnum paymentMethods;

    @ManyToOne
    private Category category;

    @ManyToOne
    private CreditCard creditCard;

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

    @Override
    public String toString() {
        return "Expenses{" +
                "id=" + id +
                ", establishment='" + establishment + '\'' +
                ", numberInstallment=" + numberInstallment +
                ", quantityInstallments=" + quantityInstallments +
                ", valuesInstallment=" + valuesInstallment +
                ", week='" + week + '\'' +
                ", purchaseDate=" + transactionDate +
                ", referenceMonth=" + referenceMonth +
                ", referenceYear=" + referenceYear +
                ", category=" + category +
                ", paymentMethods=" + paymentMethods.getDescriptor() +
                ", creditCard=" + creditCard +
                '}';
    }
}
