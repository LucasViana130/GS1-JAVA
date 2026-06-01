package br.com.fiap.agroorbit.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse extends RepresentationModel<DashboardResponse> {

    private long totalFarms;
    private long totalCropAreas;
    private long totalSensors;
    private long openAlerts;
    private long criticalAlerts;
    private BigDecimal averageNdvi;
    private long areasInRisk;
    private SensorReadingResponse latestReading;
}
