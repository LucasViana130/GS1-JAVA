package br.com.fiap.agroorbit.dtos.request;

import br.com.fiap.agroorbit.models.enums.AlertSeverity;
import br.com.fiap.agroorbit.models.enums.AlertStatus;
import br.com.fiap.agroorbit.models.enums.AlertType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClimateAlertRequest(
        @NotNull Long cropAreaId,
        @NotNull AlertType alertType,
        @NotNull AlertSeverity severity,
        @NotBlank String title,
        String description,
        AlertStatus status
) {
}
