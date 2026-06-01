package br.com.fiap.agroorbit.dtos.request;

import br.com.fiap.agroorbit.models.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull UserRole role
) {
}
