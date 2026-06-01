package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.CropAreaSatelliteSnapshot;
import br.com.fiap.agroorbit.models.CropAreaSatelliteSnapshotId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropAreaSatelliteSnapshotRepository extends JpaRepository<CropAreaSatelliteSnapshot, CropAreaSatelliteSnapshotId> {
}
