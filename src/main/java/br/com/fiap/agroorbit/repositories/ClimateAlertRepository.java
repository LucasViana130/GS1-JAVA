package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.ClimateAlert;
import br.com.fiap.agroorbit.models.enums.AlertSeverity;
import br.com.fiap.agroorbit.models.enums.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClimateAlertRepository extends JpaRepository<ClimateAlert, Long> {

    List<ClimateAlert> findByCropAreaId(Long cropAreaId);

    List<ClimateAlert> findByStatus(AlertStatus status);

    List<ClimateAlert> findBySeverity(AlertSeverity severity);

    long countByStatus(AlertStatus status);

    long countBySeverityAndStatus(AlertSeverity severity, AlertStatus status);

    void deleteByCropAreaId(Long cropAreaId);
}