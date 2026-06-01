package br.com.fiap.agroorbit.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FarmRequest(
        @NotNull Long userId,
        @NotBlank String name,
        @NotBlank String owner,
        @NotBlank String city,
        @NotBlank String state,
        String country
) {
}
