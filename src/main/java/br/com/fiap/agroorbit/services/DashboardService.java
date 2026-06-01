package br.com.fiap.agroorbit.services;

import br.com.fiap.agroorbit.dtos.response.DashboardResponse;
import br.com.fiap.agroorbit.dtos.response.SensorReadingResponse;
import br.com.fiap.agroorbit.models.SatelliteData;
import br.com.fiap.agroorbit.models.enums.AlertSeverity;
import br.com.fiap.agroorbit.models.enums.AlertStatus;
import br.com.fiap.agroorbit.repositories.ClimateAlertRepository;
import br.com.fiap.agroorbit.repositories.CropAreaRepository;
import br.com.fiap.agroorbit.repositories.FarmRepository;
import br.com.fiap.agroorbit.repositories.SatelliteDataRepository;
import br.com.fiap.agroorbit.repositories.SensorReadingRepository;
import br.com.fiap.agroorbit.repositories.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FarmRepository farmRepository;
    private final CropAreaRepository cropAreaRepository;
    private final SensorRepository sensorRepository;
    private final ClimateAlertRepository climateAlertRepository;
    private final SatelliteDataRepository satelliteDataRepository;
    private final SensorReadingRepository sensorReadingRepository;

    @Cacheable(value = "dashboard")
    public DashboardResponse getDashboard() {
        List<SatelliteData> satelliteData = satelliteDataRepository.findAll();
        BigDecimal averageNdvi = calculateAverageNdvi(satelliteData);
        SensorReadingResponse latestReading = sensorReadingRepository.findTopByOrderByReadingDateDesc()
                .map(SensorReadingResponse::fromEntity)
                .orElse(null);

        return DashboardResponse.builder()
                .totalFarms(farmRepository.count())
                .totalCropAreas(cropAreaRepository.count())
                .totalSensors(sensorRepository.count())
                .openAlerts(climateAlertRepository.countByStatus(AlertStatus.OPEN))
                .criticalAlerts(climateAlertRepository.countBySeverityAndStatus(AlertSeverity.CRITICAL, AlertStatus.OPEN))
                .averageNdvi(averageNdvi)
                .areasInRisk(cropAreaRepository.findAll().stream().filter(cropArea -> !"NORMAL".equals(cropArea.getStatus().name())).count())
                .latestReading(latestReading)
                .build();
    }

    private BigDecimal calculateAverageNdvi(List<SatelliteData> satelliteData) {
        if (satelliteData.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return satelliteData.stream()
                .map(SatelliteData::getNdviAverage)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(satelliteData.size()), 4, RoundingMode.HALF_UP);
    }
}
