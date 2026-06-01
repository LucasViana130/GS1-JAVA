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
@DiscriminatorValue("HUMIDITY")
public class HumiditySensor extends Sensor {

    public HumiditySensor(SensorRequest request, CropArea cropArea) {
        super(request, cropArea);
    }

    public HumiditySensor(CropArea cropArea, String name, SensorStatus status, LocalDate installationDate) {
        super(cropArea, name, SensorType.HUMIDITY, status, installationDate);
    }
}
