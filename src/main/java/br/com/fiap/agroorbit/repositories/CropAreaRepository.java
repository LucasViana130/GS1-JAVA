package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.CropArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropAreaRepository extends JpaRepository<CropArea, Long> {
    long countByFarmUserId(Long userId);
    java.util.List<CropArea> findByFarmId(Long farmId);
}
