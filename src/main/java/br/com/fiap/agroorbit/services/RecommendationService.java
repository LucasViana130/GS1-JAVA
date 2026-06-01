package br.com.fiap.agroorbit.services;

import br.com.fiap.agroorbit.dtos.request.RecommendationRequest;
import br.com.fiap.agroorbit.dtos.response.RecommendationResponse;
import br.com.fiap.agroorbit.exceptions.ResourceNotFoundException;
import br.com.fiap.agroorbit.models.ClimateAlert;
import br.com.fiap.agroorbit.models.Recommendation;
import br.com.fiap.agroorbit.models.enums.RecommendationPriority;
import br.com.fiap.agroorbit.repositories.ClimateAlertRepository;
import br.com.fiap.agroorbit.repositories.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository repository;
    private final ClimateAlertRepository climateAlertRepository;

    @Cacheable(value = "recommendations")
    public Page<RecommendationResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(RecommendationResponse::fromEntity);
    }

    @Cacheable(value = "recommendationsByAlert", key = "#alertId")
    public List<RecommendationResponse> findByAlert(Long alertId) {
        return repository.findByAlertId(alertId).stream().map(RecommendationResponse::fromEntity).toList();
    }

    @Cacheable(value = "recommendationsByCropArea", key = "#cropAreaId")
    public List<RecommendationResponse> findByCropArea(Long cropAreaId) {
        return repository.findByAlertCropAreaId(cropAreaId).stream().map(RecommendationResponse::fromEntity).toList();
    }

    @Cacheable(value = "recommendation", key = "#id")
    public RecommendationResponse findById(Long id) {
        return repository.findById(id)
                .map(RecommendationResponse::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Recomendação não encontrada"));
    }

    @CacheEvict(value = {"recommendations", "recommendationsByAlert", "recommendationsByCropArea", "recommendation", "dashboard"}, allEntries = true)
    public RecommendationResponse create(RecommendationRequest request) {
        ClimateAlert alert = climateAlertRepository.findById(request.alertId())
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado"));
        return RecommendationResponse.fromEntity(repository.save(new Recommendation(request, alert)));
    }

    @CacheEvict(value = {"recommendations", "recommendationsByAlert", "recommendationsByCropArea", "recommendation", "dashboard"}, allEntries = true)
    public Recommendation createFromAlert(ClimateAlert alert, String message, RecommendationPriority priority) {
        Recommendation recommendation = Recommendation.builder()
                .alert(alert)
                .message(message)
                .priority(priority)
                .build();
        return repository.save(recommendation);
    }

    @CacheEvict(value = {"recommendations", "recommendationsByAlert", "recommendationsByCropArea", "recommendation", "dashboard"}, allEntries = true)
    public RecommendationResponse update(Long id, RecommendationRequest request) {
        Recommendation recommendation = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recomendação não encontrada"));
        ClimateAlert alert = climateAlertRepository.findById(request.alertId())
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado"));
        recommendation.updateFrom(request, alert);
        return RecommendationResponse.fromEntity(repository.save(recommendation));
    }

    @CacheEvict(value = {"recommendations", "recommendationsByAlert", "recommendationsByCropArea", "recommendation", "dashboard"}, allEntries = true)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recomendação não encontrada");
        }
        repository.deleteById(id);
    }
}
