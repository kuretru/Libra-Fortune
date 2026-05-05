package com.kuretru.web.libra.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("天秤·财富")
                        .description("GitHub: https://github.com/kuretru/Libra-Fortune")
                        .contact(new Contact()
                                .name("kuretru")
                                .url("https://github.com/kuretru/Libra-Fortune")
                                .email("kuretru@gmail.com"))
                        .license(new License()
                                .name("GPLv3")
                                .url("https://www.gnu.org/licenses/gpl-3.0.html"))
                );
    }

}
