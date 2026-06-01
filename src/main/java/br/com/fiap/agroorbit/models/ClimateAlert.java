package br.com.fiap.agroorbit.models;

import br.com.fiap.agroorbit.dtos.request.ClimateAlertRequest;
import br.com.fiap.agroorbit.models.enums.AlertSeverity;
import br.com.fiap.agroorbit.models.enums.AlertStatus;
import br.com.fiap.agroorbit.models.enums.AlertType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CLIMATE_ALERT")
public class ClimateAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alert")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_crop_area", nullable = false)
    private CropArea cropArea;

    @Enumerated(EnumType.STRING)
    @Column(name = "ds_alert_type", nullable = false, length = 30)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ds_severity", nullable = false, length = 20)
    private AlertSeverity severity;

    @Column(name = "nm_title", nullable = false, length = 150)
    private String title;

    @Column(name = "ds_description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "ds_status", nullable = false, length = 20)
    private AlertStatus status;

    @Column(name = "dt_created", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "dt_resolved")
    private LocalDateTime resolvedAt;

    public ClimateAlert(ClimateAlertRequest request, CropArea cropArea) {
        this.cropArea = cropArea;
        this.alertType = request.alertType();
        this.severity = request.severity();
        this.title = request.title();
        this.description = request.description();
        this.status = request.status();
    }

    public void updateStatus(AlertStatus status) {
        this.status = status;
        if (status == AlertStatus.RESOLVED) {
            this.resolvedAt = LocalDateTime.now();
        }
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = AlertStatus.OPEN;
        }
    }
}
