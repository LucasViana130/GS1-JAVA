package br.com.fiap.agroorbit.dtos.response;

import br.com.fiap.agroorbit.models.SatelliteData;
import br.com.fiap.agroorbit.models.enums.SatelliteSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SatelliteDataResponse extends RepresentationModel<SatelliteDataResponse> {

    private Long id;
    private Long cropAreaId;
    private String cropAreaName;
    private SatelliteSource source;
    private BigDecimal ndviAverage;
    private BigDecimal ndviMin;
    private BigDecimal ndviMax;
    private BigDecimal surfaceTemperature;
    private BigDecimal cloudCoverage;
    private LocalDate captureDate;
    private LocalDateTime createdAt;

    public static SatelliteDataResponse fromEntity(SatelliteData data) {
        return SatelliteDataResponse.builder()
                .id(data.getId())
                .cropAreaId(data.getCropArea().getId())
                .cropAreaName(data.getCropArea().getName())
                .source(data.getSource())
                .ndviAverage(data.getNdviAverage())
                .ndviMin(data.getNdviMin())
                .ndviMax(data.getNdviMax())
                .surfaceTemperature(data.getSurfaceTemperature())
                .cloudCoverage(data.getCloudCoverage())
                .captureDate(data.getCaptureDate())
                .createdAt(data.getCreatedAt())
                .build();
    }
}
