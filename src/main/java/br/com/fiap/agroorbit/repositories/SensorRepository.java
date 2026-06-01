package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.Sensor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    long countByCropAreaFarmUserId(Long userId);
    List<Sensor> findByCropAreaId(Long cropAreaId);
}
