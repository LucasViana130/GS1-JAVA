package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    List<SensorReading> findBySensorId(Long sensorId);

    List<SensorReading> findBySensorCropAreaId(Long cropAreaId);

    Optional<SensorReading> findTopBySensorCropAreaIdOrderByReadingDateDesc(Long cropAreaId);

    Optional<SensorReading> findTopByOrderByReadingDateDesc();

    @Modifying
    @Query("delete from SensorReading sr where sr.sensor.id = :sensorId")
    void deleteBySensorId(@Param("sensorId") Long sensorId);

    @Modifying
    @Query("delete from SensorReading sr where sr.sensor.cropArea.id = :cropAreaId")
    void deleteBySensorCropAreaId(@Param("cropAreaId") Long cropAreaId);
}