package br.com.fiap.agroorbit.models;

import br.com.fiap.agroorbit.dtos.request.RecommendationRequest;
import br.com.fiap.agroorbit.models.enums.RecommendationPriority;
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
@Table(name = "TB_RECOMMENDATION")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recommendation")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_alert", nullable = false)
    private ClimateAlert alert;

    @Column(name = "ds_message", nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "ds_priority", nullable = false, length = 20)
    private RecommendationPriority priority;

    @Column(name = "dt_created", nullable = false)
    private LocalDateTime createdAt;

    public Recommendation(RecommendationRequest request, ClimateAlert alert) {
        this.alert = alert;
        this.message = request.message();
        this.priority = request.priority();
    }

    public void updateFrom(RecommendationRequest request, ClimateAlert alert) {
        this.alert = alert;
        this.message = request.message();
        this.priority = request.priority();
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (priority == null) {
            priority = RecommendationPriority.MEDIUM;
        }
    }
}
