package br.com.fiap.agroorbit.models;

import br.com.fiap.agroorbit.dtos.request.SensorRequest;
import br.com.fiap.agroorbit.models.enums.SensorStatus;
import br.com.fiap.agroorbit.models.enums.SensorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@DiscriminatorValue("SOIL_MOISTURE")
public class SoilMoistureSensor extends Sensor {

    public SoilMoistureSensor(SensorRequest request, CropArea cropArea) {
        super(request, cropArea);
    }

    public SoilMoistureSensor(CropArea cropArea, String name, SensorStatus status, LocalDate installationDate) {
        super(cropArea, name, SensorType.SOIL_MOISTURE, status, installationDate);
    }
}
