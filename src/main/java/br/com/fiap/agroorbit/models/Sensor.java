package br.com.fiap.agroorbit.models;

import br.com.fiap.agroorbit.dtos.request.SensorRequest;
import br.com.fiap.agroorbit.models.enums.SensorStatus;
import br.com.fiap.agroorbit.models.enums.SensorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_SENSOR")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ds_sensor_kind", discriminatorType = DiscriminatorType.STRING)
public abstract class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sensor")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_crop_area", nullable = false)
    private CropArea cropArea;

    @Column(name = "nm_name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "ds_sensor_type", nullable = false, length = 50)
    private SensorType sensorType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ds_status", nullable = false, length = 20)
    private SensorStatus status;

    @Column(name = "dt_installation", nullable = false)
    private LocalDate installationDate;

    protected Sensor(SensorRequest request, CropArea cropArea) {
        this.cropArea = cropArea;
        this.name = request.name();
        this.sensorType = request.sensorType();
        this.status = request.status();
        this.installationDate = request.installationDate();
    }

    protected Sensor(CropArea cropArea, String name, SensorType sensorType, SensorStatus status, LocalDate installationDate) {
        this.cropArea = cropArea;
        this.name = name;
        this.sensorType = sensorType;
        this.status = status;
        this.installationDate = installationDate;
    }

    public void updateFrom(SensorRequest request, CropArea cropArea) {
        this.cropArea = cropArea;
        this.name = request.name();
        this.sensorType = request.sensorType();
        this.status = request.status();
        this.installationDate = request.installationDate();
    }

    @PrePersist
    public void prePersist() {
        if (installationDate == null) {
            installationDate = LocalDate.now();
        }
        if (status == null) {
            status = SensorStatus.ACTIVE;
        }
    }
}
