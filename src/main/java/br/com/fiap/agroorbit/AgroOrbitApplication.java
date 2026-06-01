package br.com.fiap.agroorbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AgroOrbitApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgroOrbitApplication.class, args);
    }
}
