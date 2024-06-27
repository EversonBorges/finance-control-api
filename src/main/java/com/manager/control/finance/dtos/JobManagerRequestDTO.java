package com.manager.control.finance.dtos;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public record JobManagerRequestDTO(
        String name,
        LocalDateTime startExecution,
        LocalDateTime finishExecution,
        LocalDateTime nextExecution,
        String statusExecution,
        String executorMethod
) {
}


