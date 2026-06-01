package br.com.fiap.agroorbit.dtos.request;

import br.com.fiap.agroorbit.models.enums.AlertStatus;
import jakarta.validation.constraints.NotNull;

public record ClimateAlertStatusRequest(
        @NotNull AlertStatus status
) {
}
