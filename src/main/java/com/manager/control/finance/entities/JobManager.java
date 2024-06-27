package com.manager.control.finance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="job_manager")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobManager {

    @Id
    @Column(name = "start_execution")
    private LocalDateTime startExecution;

    private String name;
    @Column(name = "finish_execution")
    private LocalDateTime finishExecution;
    @Column(name = "next_execution")
    private LocalDateTime nextExecution;
    @Column(name = "status_Execution")
    private String statusExecution;
    @Column(name = "executor_method")
    private String executorMethod;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobManager that = (JobManager) o;
        return  Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
