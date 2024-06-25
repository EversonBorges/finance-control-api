package com.manager.control.finance.services;

import com.manager.control.finance.Mappers.PaymentMethodsMapper;
import com.manager.control.finance.dtos.PaymentMethodsRequestDTO;
import com.manager.control.finance.dtos.PaymentMethodsResponseDTO;
import com.manager.control.finance.entities.PaymentMethods;
import com.manager.control.finance.repositories.PaymentMethodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodsService {

    @Autowired
    private PaymentMethodsMapper paymentMethodsMapper;

    @Autowired
    private PaymentMethodsRepository paymentMethodsRepository;

    public PaymentMethods savePaymentMethods(PaymentMethodsRequestDTO dto){
        return paymentMethodsRepository.save(paymentMethodsMapper.toEntity(dto));
    }

    public PaymentMethodsResponseDTO convertPaymentMethodsToDTO(PaymentMethods paymentMethods){
        return paymentMethodsMapper.toDTO(paymentMethods);
    }
}
