package com.kuretru.web.libra.configuration;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JacksonConfigurationTest {

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JacksonConfiguration().bigDecimalModule())
            .build();

    @Test
    void serializeBigDecimalAsPlainString() throws Exception {
        var dto = new BigDecimalDTO(
                new BigDecimal("12.3400"),
                new BigDecimal("0.00000001")
        );

        var json = objectMapper.writeValueAsString(dto);

        assertEquals("{\"amount\":\"12.3400\",\"ratio\":\"0.00000001\"}", json);
    }

    @Test
    void deserializeBigDecimalFromString() throws Exception {
        var dto = objectMapper.readValue(
                "{\"amount\":\"12.3400\",\"ratio\":\"0.00000001\"}",
                BigDecimalDTO.class
        );

        assertEquals(new BigDecimal("12.3400"), dto.amount());
        assertEquals(new BigDecimal("0.00000001"), dto.ratio());
    }

    private record BigDecimalDTO(BigDecimal amount, BigDecimal ratio) {
    }

}
