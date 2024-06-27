package com.manager.control.finance.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.io.Serial;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class BrazilianCurrencyDeserializer extends StdDeserializer<BigDecimal> {

    @Serial
    private static final long serialVersionUID = 1L;

    public BrazilianCurrencyDeserializer() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        try {
            // Configurar formato para moeda brasileira
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');

            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
            decimalFormat.setParseBigDecimal(true);

            // Remover caracteres de moeda e espaços
            value = value.replaceAll("[^\\d.,-]", "");

            // Parse para BigDecimal
            return (BigDecimal) decimalFormat.parse(value);
        } catch (ParseException e) {
            throw new IOException("Unable to parse value: " + value, e);
        }
    }
}