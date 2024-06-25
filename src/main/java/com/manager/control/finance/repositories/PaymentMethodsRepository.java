package com.manager.control.finance.repositories;

import com.manager.control.finance.entities.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodsRepository extends JpaRepository<PaymentMethods, Integer> {
}
