package br.com.fiap.agroorbit.models;

import br.com.fiap.agroorbit.dtos.request.SatelliteDataRequest;
import br.com.fiap.agroorbit.models.enums.SatelliteSource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_SATELLITE_DATA")
public class SatelliteData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_satellite")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_crop_area", nullable = false)
    private CropArea cropArea;

    @Enumerated(EnumType.STRING)
    @Column(name = "ds_source", nullable = false, length = 60)
    private SatelliteSource source;

    @Column(name = "nr_ndvi_avg", nullable = false, precision = 5, scale = 4)
    private BigDecimal ndviAverage;

    @Column(name = "nr_ndvi_min", nullable = false, precision = 5, scale = 4)
    private BigDecimal ndviMin;

    @Column(name = "nr_ndvi_max", nullable = false, precision = 5, scale = 4)
    private BigDecimal ndviMax;

    @Column(name = "nr_surface_temp", precision = 5, scale = 2)
    private BigDecimal surfaceTemperature;

    @Column(name = "nr_cloud_cov", precision = 5, scale = 2)
    private BigDecimal cloudCoverage;

    @Column(name = "dt_capture", nullable = false)
    private LocalDate captureDate;

    @Column(name = "dt_created", nullable = false)
    private LocalDateTime createdAt;

    public SatelliteData(SatelliteDataRequest request, CropArea cropArea) {
        this.cropArea = cropArea;
        this.source = request.source();
        this.ndviAverage = request.ndviAverage();
        this.ndviMin = request.ndviMin();
        this.ndviMax = request.ndviMax();
        this.surfaceTemperature = request.surfaceTemperature();
        this.cloudCoverage = request.cloudCoverage();
        this.captureDate = request.captureDate();
    }

    public void updateFrom(SatelliteDataRequest request, CropArea cropArea) {
        this.cropArea = cropArea;
        this.source = request.source();
        this.ndviAverage = request.ndviAverage();
        this.ndviMin = request.ndviMin();
        this.ndviMax = request.ndviMax();
        this.surfaceTemperature = request.surfaceTemperature();
        this.cloudCoverage = request.cloudCoverage();
        this.captureDate = request.captureDate();
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
