package br.com.fiap.agroorbit.dtos.request;

import br.com.fiap.agroorbit.models.enums.SensorStatus;
import br.com.fiap.agroorbit.models.enums.SensorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SensorRequest(
        @NotNull Long cropAreaId,
        @NotBlank String name,
        @NotNull SensorType sensorType,
        SensorStatus status,
        LocalDate installationDate
) {
}
