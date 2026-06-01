package br.com.fiap.agroorbit.repositories;

import br.com.fiap.agroorbit.models.Recommendation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByAlertId(Long alertId);
    List<Recommendation> findByAlertCropAreaId(Long cropAreaId);
}
