package com.manager.control.finance.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JobManagerResponseDTO(
        String name,
        LocalDateTime startExecution,
        LocalDateTime finishExecution,
        LocalDateTime nextExecution,
        String statusExecution,
        String executorMethod
) {
}
