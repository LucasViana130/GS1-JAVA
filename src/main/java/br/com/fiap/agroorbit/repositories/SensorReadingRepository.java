package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.SensorReading;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    List<SensorReading> findBySensorId(Long sensorId);
    List<SensorReading> findBySensorCropAreaId(Long cropAreaId);
    Optional<SensorReading> findTopBySensorCropAreaIdOrderByReadingDateDesc(Long cropAreaId);
    Optional<SensorReading> findTopByOrderByReadingDateDesc();
}
