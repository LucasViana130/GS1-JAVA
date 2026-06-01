package br.com.fiap.agroorbit.dtos.response;

import br.com.fiap.agroorbit.models.ClimateAlert;
import br.com.fiap.agroorbit.models.enums.AlertSeverity;
import br.com.fiap.agroorbit.models.enums.AlertStatus;
import br.com.fiap.agroorbit.models.enums.AlertType;
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
public class ClimateAlertResponse extends RepresentationModel<ClimateAlertResponse> {

    private Long id;
    private Long cropAreaId;
    private String cropAreaName;
    private AlertType alertType;
    private AlertSeverity severity;
    private String title;
    private String description;
    private AlertStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    public static ClimateAlertResponse fromEntity(ClimateAlert alert) {
        return ClimateAlertResponse.builder()
                .id(alert.getId())
                .cropAreaId(alert.getCropArea().getId())
                .cropAreaName(alert.getCropArea().getName())
                .alertType(alert.getAlertType())
                .severity(alert.getSeverity())
                .title(alert.getTitle())
                .description(alert.getDescription())
                .status(alert.getStatus())
                .createdAt(alert.getCreatedAt())
                .resolvedAt(alert.getResolvedAt())
                .build();
    }
}
