package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.CropArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropAreaRepository extends JpaRepository<CropArea, Long> {

    List<CropArea> findByFarmId(Long farmId);
}