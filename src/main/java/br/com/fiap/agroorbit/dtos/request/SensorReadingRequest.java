package br.com.fiap.agroorbit.dtos.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SensorReadingRequest(
        @NotNull Long sensorId,
        @NotNull BigDecimal temperature,
        @NotNull @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal airHumidity,
        @NotNull @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal soilHumidity,
        Boolean manualAlert
) {
}
