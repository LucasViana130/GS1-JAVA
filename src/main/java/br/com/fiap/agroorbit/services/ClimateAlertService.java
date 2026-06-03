package br.com.fiap.agroorbit.services;

import br.com.fiap.agroorbit.dtos.request.ClimateAlertRequest;
import br.com.fiap.agroorbit.dtos.request.ClimateAlertStatusRequest;
import br.com.fiap.agroorbit.dtos.response.ClimateAlertResponse;
import br.com.fiap.agroorbit.exceptions.ResourceNotFoundException;
import br.com.fiap.agroorbit.models.ClimateAlert;
import br.com.fiap.agroorbit.models.CropArea;
import br.com.fiap.agroorbit.models.enums.AlertSeverity;
import br.com.fiap.agroorbit.models.enums.AlertStatus;
import br.com.fiap.agroorbit.models.enums.AlertType;
import br.com.fiap.agroorbit.repositories.ClimateAlertRepository;
import br.com.fiap.agroorbit.repositories.CropAreaRepository;
import br.com.fiap.agroorbit.repositories.RecommendationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClimateAlertService {

    private final RecommendationRepository recommendationRepository;
    private final ClimateAlertRepository repository;
    private final CropAreaRepository cropAreaRepository;

    @Cacheable(value = "climateAlerts")
    public Page<ClimateAlertResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(ClimateAlertResponse::fromEntity);
    }

    @Cacheable(value = "openClimateAlerts")
    public List<ClimateAlertResponse> findOpen() {
        return repository.findByStatus(AlertStatus.OPEN).stream().map(ClimateAlertResponse::fromEntity).toList();
    }

    @Cacheable(value = "criticalClimateAlerts")
    public List<ClimateAlertResponse> findCritical() {
        return repository.findBySeverity(AlertSeverity.CRITICAL).stream().map(ClimateAlertResponse::fromEntity).toList();
    }

    @Cacheable(value = "climateAlertsByCropArea", key = "#cropAreaId")
    public List<ClimateAlertResponse> findByCropArea(Long cropAreaId) {
        return repository.findByCropAreaId(cropAreaId).stream().map(ClimateAlertResponse::fromEntity).toList();
    }

    public ClimateAlert findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado"));
    }

    @Cacheable(value = "climateAlert", key = "#id")
    public ClimateAlertResponse findById(Long id) {
        return ClimateAlertResponse.fromEntity(findEntityById(id));
    }

    @CacheEvict(value = {"climateAlerts", "openClimateAlerts", "criticalClimateAlerts", "climateAlertsByCropArea", "climateAlert", "dashboard"}, allEntries = true)
    public ClimateAlertResponse create(ClimateAlertRequest request) {
        CropArea cropArea = cropAreaRepository.findById(request.cropAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Talhão não encontrado"));
        return ClimateAlertResponse.fromEntity(repository.save(new ClimateAlert(request, cropArea)));
    }

    @CacheEvict(value = {"climateAlerts", "openClimateAlerts", "criticalClimateAlerts", "climateAlertsByCropArea", "climateAlert", "dashboard"}, allEntries = true)
    public ClimateAlert createFromRisk(CropArea cropArea, AlertType type, AlertSeverity severity, String title, String description) {
        ClimateAlert alert = ClimateAlert.builder()
                .cropArea(cropArea)
                .alertType(type)
                .severity(severity)
                .title(title)
                .description(description)
                .status(AlertStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();
        return repository.save(alert);
    }

    @CacheEvict(value = {"climateAlerts", "openClimateAlerts", "criticalClimateAlerts", "climateAlertsByCropArea", "climateAlert", "dashboard"}, allEntries = true)
    public ClimateAlertResponse updateStatus(Long id, ClimateAlertStatusRequest request) {
        ClimateAlert alert = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado"));
        alert.updateStatus(request.status());
        return ClimateAlertResponse.fromEntity(repository.save(alert));
    }

    @CacheEvict(value = {"climateAlerts", "openClimateAlerts", "criticalClimateAlerts", "climateAlertsByCropArea", "climateAlert", "dashboard"}, allEntries = true)
    public ClimateAlertResponse resolve(Long id) {
        ClimateAlert alert = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado"));
        alert.updateStatus(AlertStatus.RESOLVED);
        return ClimateAlertResponse.fromEntity(repository.save(alert));
    }

    @CacheEvict(value = {"climateAlerts", "openClimateAlerts", "criticalClimateAlerts", "climateAlertsByCropArea", "climateAlert", "recommendations", "recommendationsByAlert", "recommendationsByCropArea", "dashboard"}, allEntries = true)
    @Transactional
    public void delete(Long id) {
        ClimateAlert alert = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado"));

        recommendationRepository.deleteByAlertId(alert.getId());

        repository.delete(alert);
    }
}
