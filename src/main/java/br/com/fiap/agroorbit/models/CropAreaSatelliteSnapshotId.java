package br.com.fiap.agroorbit.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CropAreaSatelliteSnapshotId implements Serializable {

    @Column(name = "id_crop_area")
    private Long cropAreaId;

    @Column(name = "dt_capture")
    private LocalDate captureDate;
}
