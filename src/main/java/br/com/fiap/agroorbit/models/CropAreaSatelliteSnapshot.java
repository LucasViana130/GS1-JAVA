package br.com.fiap.agroorbit.models;

import br.com.fiap.agroorbit.models.enums.SatelliteSource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CROP_AREA_SATELLITE_SNAPSHOT")
public class CropAreaSatelliteSnapshot {

    @EmbeddedId
    private CropAreaSatelliteSnapshotId id;

    @ManyToOne
    @MapsId("cropAreaId")
    @JoinColumn(name = "id_crop_area", nullable = false)
    private CropArea cropArea;

    @Column(name = "nr_ndvi_avg", nullable = false, precision = 5, scale = 4)
    private BigDecimal ndviAverage;

    @Enumerated(EnumType.STRING)
    @Column(name = "ds_source", nullable = false, length = 60)
    private SatelliteSource source;
}
