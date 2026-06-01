package br.com.fiap.agroorbit.services;

import br.com.fiap.agroorbit.dtos.response.RiskAnalysisResponse;
import br.com.fiap.agroorbit.exceptions.BusinessException;
import br.com.fiap.agroorbit.models.*;
import br.com.fiap.agroorbit.models.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RiskAnalysisService {
    private final CropAreaService cropAreaService;
    private final SensorReadingService sensorReadingService;
    private final SatelliteDataService satelliteDataService;
    private final ClimateAlertService climateAlertService;
    private final RecommendationService recommendationService;

    @CacheEvict(value = {"dashboard", "cropAreas", "cropArea", "climateAlerts", "openClimateAlerts", "criticalClimateAlerts", "climateAlertsByCropArea", "recommendations", "recommendationsByAlert", "recommendationsByCropArea"}, allEntries = true)
    public RiskAnalysisResponse analyze(Long cropAreaId) {
        CropArea cropArea = cropAreaService.findEntityById(cropAreaId);
        SensorReading reading = sensorReadingService.findLatestEntityByCropArea(cropAreaId).orElseThrow(() -> new BusinessException("No sensor reading found for this crop area"));
        SatelliteData satellite = satelliteDataService.findLatestEntityByCropArea(cropAreaId).orElseThrow(() -> new BusinessException("No satellite data found for this crop area"));

        BigDecimal ndvi = satellite.getNdviAverage();
        BigDecimal soil = reading.getSoilHumidity();
        BigDecimal temp = reading.getTemperature();
        BigDecimal air = reading.getAirHumidity();

        AlertType type = null;
        AlertSeverity severity = AlertSeverity.LOW;
        CropAreaStatus status = CropAreaStatus.NORMAL;
        String recommendation = "Monitorar o talhão e manter acompanhamento periódico.";

        if (ndvi.compareTo(new BigDecimal("0.45")) < 0 && soil.compareTo(new BigDecimal("30")) < 0 && temp.compareTo(new BigDecimal("32")) > 0) {
            type = AlertType.DROUGHT;
            severity = AlertSeverity.HIGH;
            status = CropAreaStatus.DROUGHT_RISK;
            recommendation = "Verificar irrigação nas próximas horas e acompanhar novo NDVI.";
        } else if (temp.compareTo(new BigDecimal("35")) > 0 && air.compareTo(new BigDecimal("30")) < 0 && ndvi.compareTo(new BigDecimal("0.40")) < 0) {
            type = AlertType.FIRE_RISK;
            severity = AlertSeverity.CRITICAL;
            status = CropAreaStatus.FIRE_RISK;
            recommendation = "Acionar monitoramento preventivo e evitar queimadas controladas na região.";
        } else if (ndvi.compareTo(new BigDecimal("0.45")) < 0) {
            type = AlertType.VEGETATION_STRESS;
            severity = AlertSeverity.MEDIUM;
            status = CropAreaStatus.STRESS;
            recommendation = "Verificar irrigação, pragas, solo ou falha de plantio no talhão.";
        } else if ("Y".equals(reading.getManualAlert())) {
            type = AlertType.SENSOR_CRITICAL;
            severity = AlertSeverity.HIGH;
            status = CropAreaStatus.ATTENTION;
            recommendation = "Verificar presencialmente a condição indicada pelo sensor.";
        }

        cropAreaService.updateStatus(cropArea, status);
        boolean generated = false;
        if (type != null) {
            ClimateAlert alert = climateAlertService.createFromRisk(cropArea, type, severity, buildTitle(type, cropArea.getName()), buildDescription(type, satellite, reading));
            recommendationService.createFromAlert(alert, recommendation, RecommendationPriority.valueOf(severity.name()));
            generated = true;
        }

        return RiskAnalysisResponse.builder().cropAreaId(cropArea.getId()).cropAreaName(cropArea.getName()).status(status).alertGenerated(generated).alertType(type).severity(type == null ? null : severity).recommendation(recommendation).build();
    }

    private String buildTitle(AlertType type, String cropAreaName) {
        return switch (type) {
            case DROUGHT -> "Risco de seca no " + cropAreaName;
            case FIRE_RISK -> "Risco de queimada no " + cropAreaName;
            case VEGETATION_STRESS -> "Estresse vegetativo no " + cropAreaName;
            case SENSOR_CRITICAL -> "Condição crítica detectada no " + cropAreaName;
        };
    }

    private String buildDescription(AlertType type, SatelliteData satellite, SensorReading reading) {
        return "Análise gerada com NDVI " + satellite.getNdviAverage() + ", temperatura " + reading.getTemperature() + "°C, umidade do ar " + reading.getAirHumidity() + "% e umidade do solo " + reading.getSoilHumidity() + "%. Tipo: " + type + ".";
    }
}
