package com.manager.control.finance.services;

import com.manager.control.finance.Mappers.JobManagerMapper;
import com.manager.control.finance.dtos.JobManagerRequestDTO;
import com.manager.control.finance.dtos.JobManagerResponseDTO;
import com.manager.control.finance.entities.JobManager;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.JobManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobManagerService {

    @Autowired
    private JobManagerMapper jobManagerMapper;

    @Autowired
    private JobManagerRepository jobManagerRepository;

    public JobManagerResponseDTO convertJobManagerToDTO(JobManager jobManager){
        return jobManagerMapper.toDTO(jobManager);
    }

    public JobManager saveJobManager(JobManager dto){
        return jobManagerRepository.save(dto);
    }

    public List<JobManagerResponseDTO> findAll() {
        return jobManagerRepository.findAll().stream().map(jobManagerMapper::toDTO).toList();
    }

    public List<JobManagerResponseDTO> findByName(String name) {
        List<Optional<JobManager>> result = jobManagerRepository.findByName(name);

        return result.stream().map(item -> jobManagerMapper.toDTO(
                item.orElseThrow(DataNotFoundException::new))).toList();
    }
}
