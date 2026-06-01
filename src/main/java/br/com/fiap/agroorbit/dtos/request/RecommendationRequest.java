package br.com.fiap.agroorbit.dtos.request;

import br.com.fiap.agroorbit.models.enums.RecommendationPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecommendationRequest(
        @NotNull Long alertId,
        @NotBlank String message,
        RecommendationPriority priority
) {
}
