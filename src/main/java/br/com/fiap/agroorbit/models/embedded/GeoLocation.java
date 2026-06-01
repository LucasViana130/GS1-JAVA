package br.com.fiap.agroorbit.models.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class GeoLocation {
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Column(name = "nr_latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Column(name = "nr_longitude", precision = 9, scale = 6)
    private BigDecimal longitude;
}
