package dev.quantara.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI quantaraOpenApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Quantara Core API")
                .description(
                    "Backend API for the Quantara Soroban developer infrastructure platform")
                .version("v0.1.0")
                .license(
                    new License()
                        .name("MIT")
                        .url("https://github.com/quantarahq/quantara-core/blob/main/LICENSE"))
                .contact(new Contact().name("Quantara").url("https://github.com/quantarahq")));
  }
}
