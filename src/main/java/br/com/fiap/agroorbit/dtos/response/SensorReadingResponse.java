package br.com.fiap.agroorbit.dtos.response;

import br.com.fiap.agroorbit.models.SensorReading;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorReadingResponse extends RepresentationModel<SensorReadingResponse> {

    private Long id;
    private Long sensorId;
    private String sensorName;
    private Long cropAreaId;
    private String cropAreaName;
    private BigDecimal temperature;
    private BigDecimal airHumidity;
    private BigDecimal soilHumidity;
    private Boolean manualAlert;
    private LocalDateTime readingDate;

    public static SensorReadingResponse fromEntity(SensorReading reading) {
        return SensorReadingResponse.builder()
                .id(reading.getId())
                .sensorId(reading.getSensor().getId())
                .sensorName(reading.getSensor().getName())
                .cropAreaId(reading.getSensor().getCropArea().getId())
                .cropAreaName(reading.getSensor().getCropArea().getName())
                .temperature(reading.getTemperature())
                .airHumidity(reading.getAirHumidity())
                .soilHumidity(reading.getSoilHumidity())
                .manualAlert("Y".equals(reading.getManualAlert()))
                .readingDate(reading.getReadingDate())
                .build();
    }
}
