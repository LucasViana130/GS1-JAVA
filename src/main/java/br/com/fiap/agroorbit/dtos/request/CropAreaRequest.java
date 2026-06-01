package br.com.fiap.agroorbit.dtos.request;

import br.com.fiap.agroorbit.models.enums.AreaUnit;
import br.com.fiap.agroorbit.models.enums.CropAreaStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CropAreaRequest(
        @NotNull Long farmId,
        @NotBlank String name,
        @NotBlank String cropType,
        @NotNull @Positive BigDecimal areaSize,
        AreaUnit areaUnit,
        BigDecimal latitude,
        BigDecimal longitude,
        String boundaryGeoJson,
        CropAreaStatus status
) {
}