package br.com.fiap.agroorbit.dtos.response;

import br.com.fiap.agroorbit.models.Recommendation;
import br.com.fiap.agroorbit.models.enums.RecommendationPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse extends RepresentationModel<RecommendationResponse> {

    private Long id;
    private Long alertId;
    private String alertTitle;
    private String message;
    private RecommendationPriority priority;
    private LocalDateTime createdAt;

    public static RecommendationResponse fromEntity(Recommendation recommendation) {
        return RecommendationResponse.builder()
                .id(recommendation.getId())
                .alertId(recommendation.getAlert().getId())
                .alertTitle(recommendation.getAlert().getTitle())
                .message(recommendation.getMessage())
                .priority(recommendation.getPriority())
                .createdAt(recommendation.getCreatedAt())
                .build();
    }
}
