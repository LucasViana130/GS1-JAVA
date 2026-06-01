package br.com.fiap.agroorbit.dtos.request;

import br.com.fiap.agroorbit.models.enums.SatelliteSource;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SatelliteDataRequest(
        @NotNull Long cropAreaId,
        @NotNull SatelliteSource source,
        @NotNull @DecimalMin("-1.0") @DecimalMax("1.0") BigDecimal ndviAverage,
        @NotNull @DecimalMin("-1.0") @DecimalMax("1.0") BigDecimal ndviMin,
        @NotNull @DecimalMin("-1.0") @DecimalMax("1.0") BigDecimal ndviMax,
        BigDecimal surfaceTemperature,
        @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal cloudCoverage,
        @NotNull LocalDate captureDate
) {
}
