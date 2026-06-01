package br.com.fiap.agroorbit.dtos.response;

import br.com.fiap.agroorbit.models.Sensor;
import br.com.fiap.agroorbit.models.enums.SensorStatus;
import br.com.fiap.agroorbit.models.enums.SensorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorResponse extends RepresentationModel<SensorResponse> {

    private Long id;
    private Long cropAreaId;
    private String cropAreaName;
    private String name;
    private SensorType sensorType;
    private SensorStatus status;
    private LocalDate installationDate;

    public static SensorResponse fromEntity(Sensor sensor) {
        return SensorResponse.builder()
                .id(sensor.getId())
                .cropAreaId(sensor.getCropArea().getId())
                .cropAreaName(sensor.getCropArea().getName())
                .name(sensor.getName())
                .sensorType(sensor.getSensorType())
                .status(sensor.getStatus())
                .installationDate(sensor.getInstallationDate())
                .build();
    }
}
