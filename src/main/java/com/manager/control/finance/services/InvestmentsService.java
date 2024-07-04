package com.manager.control.finance.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.manager.control.finance.Mappers.InvestmentsMapper;
import com.manager.control.finance.dtos.InvestmentsRequestDTO;
import com.manager.control.finance.dtos.InvestmentsResponseDTO;
import com.manager.control.finance.entities.Investments;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.InvestmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvestmentsService {

    @Autowired
    private InvestmentsMapper investmentsMapper;

    @Autowired
    private InvestmentsRepository investmentsRepository;

    public InvestmentsResponseDTO convertInvestmentsToDTO(Investments investments){
        return investmentsMapper.toDTO(investments);
    }

    public Investments saveInvestments(InvestmentsRequestDTO dto){

        var year = dto.transactionDate().getYear();
        var month = dto.transactionDate().getMonth().getValue();
        Investments investments = investmentsMapper.toEntity(dto);
        investments.setReferenceYear(year);
        investments.setReferenceMonth(month);

        return investmentsRepository.save(investments);
    }

    public InvestmentsResponseDTO findById(Integer id) {
        Optional<Investments> result = investmentsRepository.findById(id);

        return investmentsMapper.toDTO(result.orElseThrow(DataNotFoundException::new));
    }

    public List<InvestmentsResponseDTO> findAll() {
        return investmentsRepository.findAll().stream().map(investmentsMapper::toDTO).toList();
    }

    public void delete(Integer id) {
        investmentsRepository.deleteById(id);
    }

    public InvestmentsResponseDTO update(InvestmentsRequestDTO dto) {
        Optional<Investments> result = investmentsRepository.findById(dto.id());

        if (result.isPresent()){
            Investments investments = result.get();
            try {
                Investments updated = investmentsRepository.save(investmentsMapper.update(investments,dto));
                return investmentsMapper.toDTO(updated);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            }
        }else{
            throw new DataNotFoundException();
        }
    }
}
