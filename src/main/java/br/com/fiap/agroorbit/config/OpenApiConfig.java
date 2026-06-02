package br.com.fiap.agroorbit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        Server productionServer = new Server();
        productionServer.setUrl("https://gs1-java-production.up.railway.app");
        productionServer.setDescription("Railway Production");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development");

        SecurityScheme securityScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(SECURITY_SCHEME_NAME);

        return new OpenAPI()
                .servers(List.of(productionServer, localServer))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme))
                .addSecurityItem(securityRequirement)
                .info(new Info()
                        .title("AgroOrbit API")
                        .version("1.0.0")
                        .description("API REST da solução AgroOrbit para monitoramento agrícola com sensores simulados, dados satelitais, alertas e recomendações."));
    }
}