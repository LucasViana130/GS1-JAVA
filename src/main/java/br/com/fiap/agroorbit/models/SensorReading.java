package br.com.fiap.agroorbit.models;

import br.com.fiap.agroorbit.dtos.request.SensorReadingRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_SENSOR_READING")
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reading")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_sensor", nullable = false)
    private Sensor sensor;

    @Column(name = "nr_temperature", nullable = false, precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(name = "nr_air_humidity", nullable = false, precision = 5, scale = 2)
    private BigDecimal airHumidity;

    @Column(name = "nr_soil_humidity", nullable = false, precision = 5, scale = 2)
    private BigDecimal soilHumidity;

    @Column(name = "fl_manual_alert", length = 1)
    private String manualAlert;

    @Column(name = "dt_reading", nullable = false)
    private LocalDateTime readingDate;

    public SensorReading(SensorReadingRequest request, Sensor sensor) {
        this.sensor = sensor;
        this.temperature = request.temperature();
        this.airHumidity = request.airHumidity();
        this.soilHumidity = request.soilHumidity();
        this.manualAlert = Boolean.TRUE.equals(request.manualAlert()) ? "Y" : "N";
    }

    @PrePersist
    public void prePersist() {
        if (readingDate == null) {
            readingDate = LocalDateTime.now();
        }
        if (manualAlert == null) {
            manualAlert = "N";
        }
    }
}
