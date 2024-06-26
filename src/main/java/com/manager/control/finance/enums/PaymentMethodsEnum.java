package com.manager.control.finance.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.manager.control.finance.configuration.TipoPagamentoDeserializer;
import lombok.Getter;

@Getter
@JsonDeserialize(using = TipoPagamentoDeserializer.class)
public enum PaymentMethodsEnum {

    CREDIT_CARD("CRT", "Cartão de crédito"),
    DEBIT_CARD("DBT", "Cartão de débito"),
    PIX("PIX", "Pix"),
    FEED_CARD("ALM", "Alimentação"),
    ACCOUNT_DISCOUNT("DSC", "Desconto em conta");

    private final String id;
    private final String descriptor;


    PaymentMethodsEnum(String id, String descriptor) {
        this.id = id;
        this.descriptor = descriptor;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", descriptor, id);
    }
}
