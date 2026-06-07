package com.kuretru.web.libra.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Module bigDecimalModule() {
        var module = new SimpleModule();
        module.addSerializer(BigDecimal.class, new BigDecimalJsonSerializer());
        return module;
    }

    private static class BigDecimalJsonSerializer extends JsonSerializer<BigDecimal> {

        @Override
        public void serialize(BigDecimal value, JsonGenerator generator, SerializerProvider serializers)
                throws IOException {
            generator.writeString(value.toPlainString());
        }

    }

}
