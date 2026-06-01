package br.com.fiap.agroorbit.models;

import br.com.fiap.agroorbit.dtos.request.CropAreaRequest;
import br.com.fiap.agroorbit.models.embedded.GeoLocation;
import br.com.fiap.agroorbit.models.enums.AreaUnit;
import br.com.fiap.agroorbit.models.enums.CropAreaStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
@Table(name = "TB_CROP_AREA")
public class CropArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_crop_area")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_farm", nullable = false)
    private Farm farm;

    @Column(name = "nm_name", nullable = false, length = 100)
    private String name;

    @Column(name = "ds_crop_type", nullable = false, length = 80)
    private String cropType;

    @Column(name = "nr_area_size", nullable = false, precision = 10, scale = 2)
    private BigDecimal areaSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "ds_area_unit", nullable = false, length = 20)
    private AreaUnit areaUnit;

    @Embedded
    private GeoLocation location;

    @Lob
    @Column(name = "ds_boundary_geojson", columnDefinition = "CLOB")
    private String boundaryGeoJson;

    @Enumerated(EnumType.STRING)
    @Column(name = "ds_status", nullable = false, length = 30)
    private CropAreaStatus status;

    @Column(name = "dt_created", nullable = false)
    private LocalDateTime createdAt;

    public CropArea(CropAreaRequest request, Farm farm) {
        this.farm = farm;
        this.name = request.name();
        this.cropType = request.cropType();
        this.areaSize = request.areaSize();
        this.areaUnit = request.areaUnit() == null ? AreaUnit.HA : request.areaUnit();
        this.location = new GeoLocation(request.latitude(), request.longitude());
        this.boundaryGeoJson = request.boundaryGeoJson();
        this.status = request.status() == null ? CropAreaStatus.NORMAL : request.status();
    }

    public void updateFrom(CropAreaRequest request, Farm farm) {
        this.farm = farm;
        this.name = request.name();
        this.cropType = request.cropType();
        this.areaSize = request.areaSize();
        this.areaUnit = request.areaUnit() == null ? this.areaUnit : request.areaUnit();
        this.location = new GeoLocation(request.latitude(), request.longitude());
        this.boundaryGeoJson = request.boundaryGeoJson();

        if (request.status() != null) {
            this.status = request.status();
        }
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null) {
            status = CropAreaStatus.NORMAL;
        }

        if (areaUnit == null) {
            areaUnit = AreaUnit.HA;
        }
    }
}