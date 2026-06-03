package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    List<Sensor> findByCropAreaId(Long cropAreaId);

    void deleteByCropAreaId(Long cropAreaId);
}