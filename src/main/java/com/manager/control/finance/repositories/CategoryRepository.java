package com.manager.control.finance.repositories;

import com.manager.control.finance.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByType(Character type);

    Category findByDescription(String description);
}
