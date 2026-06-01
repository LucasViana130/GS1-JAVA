package br.com.fiap.agroorbit.dtos.response;

import br.com.fiap.agroorbit.models.CropArea;
import br.com.fiap.agroorbit.models.enums.CropAreaStatus;
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
public class CropAreaResponse extends RepresentationModel<CropAreaResponse> {

    private Long id;
    private Long farmId;
    private String farmName;
    private String name;
    private String cropType;
    private BigDecimal areaSize;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private CropAreaStatus status;
    private LocalDateTime createdAt;

    public static CropAreaResponse fromEntity(CropArea cropArea) {
        BigDecimal latitude = cropArea.getLocation() == null ? null : cropArea.getLocation().getLatitude();
        BigDecimal longitude = cropArea.getLocation() == null ? null : cropArea.getLocation().getLongitude();

        return CropAreaResponse.builder()
                .id(cropArea.getId())
                .farmId(cropArea.getFarm().getId())
                .farmName(cropArea.getFarm().getName())
                .name(cropArea.getName())
                .cropType(cropArea.getCropType())
                .areaSize(cropArea.getAreaSize())
                .latitude(latitude)
                .longitude(longitude)
                .status(cropArea.getStatus())
                .createdAt(cropArea.getCreatedAt())
                .build();
    }
}
