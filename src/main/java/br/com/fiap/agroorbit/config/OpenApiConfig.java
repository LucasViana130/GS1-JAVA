package br.com.fiap.agroorbit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server productionServer = new Server();
        productionServer.setUrl("https://gs1-java-production.up.railway.app");
        productionServer.setDescription("Railway Production");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development");

        return new OpenAPI()
                .servers(List.of(productionServer, localServer))
                .info(new Info()
                        .title("AgroOrbit API")
                        .version("1.0.0")
                        .description("API REST da solução AgroOrbit para monitoramento agrícola com sensores simulados, dados satelitais, alertas e recomendações."));
    }
}