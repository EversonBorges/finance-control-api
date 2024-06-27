package com.manager.control.finance.Mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.control.finance.dtos.JobManagerRequestDTO;
import com.manager.control.finance.dtos.JobManagerResponseDTO;
import com.manager.control.finance.entities.JobManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobManagerMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public JobManagerResponseDTO toDTO(JobManager jobManager) {
        return objectMapper.convertValue(jobManager, JobManagerResponseDTO.class);
    }

    public JobManager toEntity(JobManagerRequestDTO jobManagerRequestDTO) {
        return objectMapper.convertValue(jobManagerRequestDTO, JobManager.class);
    }
}
