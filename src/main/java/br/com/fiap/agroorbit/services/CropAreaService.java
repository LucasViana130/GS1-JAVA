package br.com.fiap.agroorbit.services;

import br.com.fiap.agroorbit.dtos.request.CropAreaRequest;
import br.com.fiap.agroorbit.dtos.response.CropAreaResponse;
import br.com.fiap.agroorbit.exceptions.ResourceNotFoundException;
import br.com.fiap.agroorbit.models.CropArea;
import br.com.fiap.agroorbit.models.Farm;
import br.com.fiap.agroorbit.models.enums.CropAreaStatus;
import br.com.fiap.agroorbit.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.fiap.agroorbit.repositories.RecommendationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CropAreaService {

    private final CropAreaRepository repository;
    private final FarmRepository farmRepository;
    private final RecommendationRepository recommendationRepository;
    private final ClimateAlertRepository climateAlertRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;
    private final SatelliteDataRepository satelliteDataRepository;
    private final CropAreaSatelliteSnapshotRepository cropAreaSatelliteSnapshotRepository;

    @Cacheable(value = "cropAreas")
    public Page<CropAreaResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(CropAreaResponse::fromEntity);
    }

    @Cacheable(value = "cropAreasByFarm", key = "#farmId")
    public List<CropAreaResponse> findByFarm(Long farmId) {
        return repository.findByFarmId(farmId).stream().map(CropAreaResponse::fromEntity).toList();
    }

    public CropArea findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Talhão não encontrado"));
    }

    @Cacheable(value = "cropArea", key = "#id")
    public CropAreaResponse findById(Long id) {
        return CropAreaResponse.fromEntity(findEntityById(id));
    }

    @CacheEvict(value = {"cropAreas", "cropAreasByFarm", "dashboard"}, allEntries = true)
    public CropAreaResponse create(CropAreaRequest request) {
        Farm farm = farmRepository.findById(request.farmId())
                .orElseThrow(() -> new ResourceNotFoundException("Fazenda não encontrada"));
        return CropAreaResponse.fromEntity(repository.save(new CropArea(request, farm)));
    }

    @CacheEvict(value = {"cropAreas", "cropAreasByFarm", "cropArea", "dashboard"}, allEntries = true)
    public CropAreaResponse update(Long id, CropAreaRequest request) {
        CropArea cropArea = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Talhão não encontrado"));
        Farm farm = farmRepository.findById(request.farmId())
                .orElseThrow(() -> new ResourceNotFoundException("Fazenda não encontrada"));
        cropArea.updateFrom(request, farm);
        return CropAreaResponse.fromEntity(repository.save(cropArea));
    }

    @CacheEvict(value = {"cropAreas", "cropAreasByFarm", "cropArea", "dashboard"}, allEntries = true)
    public CropArea updateStatus(CropArea cropArea, CropAreaStatus status) {
        cropArea.setStatus(status);
        return repository.save(cropArea);
    }
    @Transactional
    public void deleteCropAreaWithChildren(Long cropAreaId) {
        CropArea cropArea = repository.findById(cropAreaId)
                .orElseThrow(() -> new ResourceNotFoundException("Talhão não encontrado"));

        recommendationRepository.deleteByAlertCropAreaId(cropArea.getId());

        climateAlertRepository.deleteByCropAreaId(cropArea.getId());

        sensorReadingRepository.deleteBySensorCropAreaId(cropArea.getId());

        sensorRepository.deleteByCropAreaId(cropArea.getId());

        satelliteDataRepository.deleteByCropAreaId(cropArea.getId());

        cropAreaSatelliteSnapshotRepository.deleteByIdCropAreaId(cropArea.getId());

        repository.delete(cropArea);
    }

    @Transactional
    @CacheEvict(value = {
            "crop-areas",
            "farms",
            "sensors",
            "sensor-readings",
            "satellite-data",
            "climate-alerts",
            "recommendations",
            "dashboard"
    }, allEntries = true)
    public void delete(Long id) {
        deleteCropAreaWithChildren(id);
    }
}
