package br.com.fiap.agroorbit.models;

import br.com.fiap.agroorbit.dtos.request.FarmRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_FARM")
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_farm")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(name = "nm_name", nullable = false, length = 100)
    private String name;

    @Column(name = "nm_owner", nullable = false, length = 100)
    private String owner;

    @Column(name = "nm_city", nullable = false, length = 80)
    private String city;

    @Column(name = "nm_state", nullable = false, length = 50)
    private String state;

    @Column(name = "nm_country", nullable = false, length = 50)
    private String country;

    @Column(name = "dt_created", nullable = false)
    private LocalDateTime createdAt;

    public Farm(FarmRequest request, User user) {
        this.user = user;
        this.name = request.name();
        this.owner = request.owner();
        this.city = request.city();
        this.state = request.state();
        this.country = request.country();
    }

    public void updateFrom(FarmRequest request, User user) {
        this.user = user;
        this.name = request.name();
        this.owner = request.owner();
        this.city = request.city();
        this.state = request.state();
        this.country = request.country();
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (country == null || country.isBlank()) {
            country = "Brasil";
        }
    }
}
