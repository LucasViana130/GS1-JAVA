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
@DiscriminatorValue("WEATHER_STATION")
public class WeatherStationSensor extends Sensor {

    public WeatherStationSensor(SensorRequest request, CropArea cropArea) {
        super(request, cropArea);
    }

    public WeatherStationSensor(CropArea cropArea, String name, SensorStatus status, LocalDate installationDate) {
        super(cropArea, name, SensorType.WEATHER_STATION, status, installationDate);
    }
}
