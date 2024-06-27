package com.manager.control.finance.repositories;

import com.manager.control.finance.entities.Category;
import com.manager.control.finance.entities.JobManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobManagerRepository extends JpaRepository<JobManager, Integer> {

    List<Optional<JobManager>> findByName(String name);
}
