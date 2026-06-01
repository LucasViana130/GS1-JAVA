package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.SatelliteData;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SatelliteDataRepository extends JpaRepository<SatelliteData, Long> {
    List<SatelliteData> findByCropAreaId(Long cropAreaId);
    Optional<SatelliteData> findTopByCropAreaIdOrderByCaptureDateDescCreatedAtDesc(Long cropAreaId);
}
