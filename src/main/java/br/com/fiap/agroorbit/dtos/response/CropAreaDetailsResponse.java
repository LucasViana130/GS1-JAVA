package br.com.fiap.agroorbit.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CropAreaDetailsResponse extends RepresentationModel<CropAreaDetailsResponse> {

    private CropAreaResponse cropArea;
    private SatelliteDataResponse latestSatelliteData;
    private SensorReadingResponse latestReading;
    private long openAlerts;
    private String mainRecommendation;
}
