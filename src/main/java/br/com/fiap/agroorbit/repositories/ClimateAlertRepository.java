package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.ClimateAlert;
import br.com.fiap.agroorbit.models.enums.AlertSeverity;
import br.com.fiap.agroorbit.models.enums.AlertStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClimateAlertRepository extends JpaRepository<ClimateAlert, Long> {
    List<ClimateAlert> findByCropAreaId(Long cropAreaId);
    List<ClimateAlert> findByStatus(AlertStatus status);
    List<ClimateAlert> findBySeverity(AlertSeverity severity);
    long countByStatus(AlertStatus status);
    long countBySeverityAndStatus(AlertSeverity severity, AlertStatus status);
}
