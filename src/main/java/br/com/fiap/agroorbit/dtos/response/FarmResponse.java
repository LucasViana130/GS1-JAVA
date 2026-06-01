package br.com.fiap.agroorbit.dtos.response;

import br.com.fiap.agroorbit.models.Farm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarmResponse extends RepresentationModel<FarmResponse> {

    private Long id;
    private Long userId;
    private String userName;
    private String name;
    private String owner;
    private String city;
    private String state;
    private String country;
    private LocalDateTime createdAt;

    public static FarmResponse fromEntity(Farm farm) {
        return FarmResponse.builder()
                .id(farm.getId())
                .userId(farm.getUser().getId())
                .userName(farm.getUser().getName())
                .name(farm.getName())
                .owner(farm.getOwner())
                .city(farm.getCity())
                .state(farm.getState())
                .country(farm.getCountry())
                .createdAt(farm.getCreatedAt())
                .build();
    }
}
