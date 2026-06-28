package com.kuretru.web.libra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;

import java.math.BigDecimal;

@Configuration
public class JacksonConfiguration {

    @Bean
    public JacksonModule bigDecimalModule() {
        var module = new SimpleModule();
        module.addSerializer(BigDecimal.class, new BigDecimalJsonSerializer());
        return module;
    }

    private static class BigDecimalJsonSerializer extends ValueSerializer<BigDecimal> {

        @Override
        public void serialize(BigDecimal value, JsonGenerator generator, SerializationContext serializers)
                throws JacksonException {
            generator.writeString(value.toPlainString());
        }

    }

}
