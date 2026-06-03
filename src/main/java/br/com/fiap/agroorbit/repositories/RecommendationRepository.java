package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByAlertId(Long alertId);

    List<Recommendation> findByAlertCropAreaId(Long cropAreaId);

    void deleteByAlertId(Long alertId);

    void deleteByAlertCropAreaId(Long cropAreaId);
}