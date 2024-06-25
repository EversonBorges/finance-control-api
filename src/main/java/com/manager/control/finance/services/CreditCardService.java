package com.manager.control.finance.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.manager.control.finance.Mappers.CreditCardMapper;
import com.manager.control.finance.dtos.CreditCardRequestDTO;
import com.manager.control.finance.dtos.CreditCardResponseDTO;
import com.manager.control.finance.entities.CreditCard;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditCardService {
    @Autowired
    private CreditCardRepository repository;

    @Autowired
    private CreditCardMapper mapper;

    public CreditCard create(CreditCardRequestDTO dto) {
        return repository.save(mapper.toEntity(dto));
    }

    public CreditCardResponseDTO convertCreditCardToDTO(CreditCard CreditCard) {
        return mapper.toDTO(CreditCard);
    }

    public List<CreditCardResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    public CreditCardResponseDTO findById(Integer id) {
        Optional<CreditCard> result = repository.findById(id);

        return mapper.toDTO(result.orElseThrow(DataNotFoundException::new));
    }

    public CreditCardResponseDTO update(CreditCardRequestDTO dto) {
        Optional<CreditCard> result = repository.findById(dto.id());

         if (result.isPresent()){
             CreditCard creditCard = result.get();
             try {
                 CreditCard updated = repository.save(mapper.update(creditCard,dto));
                 return mapper.toDTO(updated);
             } catch (JsonMappingException e) {
                 throw new RuntimeException(e);
             }
         }else{
             throw new DataNotFoundException();
         }
    }

    public CreditCard findCreditCardById(Integer id) {
        Optional<CreditCard> result = repository.findById(id);
        return result.orElseThrow(DataNotFoundException::new);
    }
}
