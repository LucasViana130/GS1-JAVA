package br.com.fiap.agroorbit.dtos.response;

import br.com.fiap.agroorbit.models.enums.AlertSeverity;
import br.com.fiap.agroorbit.models.enums.AlertType;
import br.com.fiap.agroorbit.models.enums.CropAreaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskAnalysisResponse extends RepresentationModel<RiskAnalysisResponse> {

    private Long cropAreaId;
    private String cropAreaName;
    private CropAreaStatus status;
    private boolean alertGenerated;
    private AlertType alertType;
    private AlertSeverity severity;
    private String recommendation;
}
