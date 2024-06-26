package com.manager.control.finance.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.manager.control.finance.enums.PaymentMethodsEnum;
import com.manager.control.finance.utils.GlobalMessages;

import java.io.IOException;

public class PaymentMethodsDeserializer extends JsonDeserializer<PaymentMethodsEnum> {

    @Override
    public PaymentMethodsEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String code = jsonParser.getText();
        for (PaymentMethodsEnum type : PaymentMethodsEnum.values()) {
            if (type.getId().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException(GlobalMessages.ILLEGAL_ARGUMENTS_ERROR + code);
    }
}
