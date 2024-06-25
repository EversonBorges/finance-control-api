package com.manager.control.finance.repositories;

import com.manager.control.finance.entities.Category;
import com.manager.control.finance.entities.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
}
