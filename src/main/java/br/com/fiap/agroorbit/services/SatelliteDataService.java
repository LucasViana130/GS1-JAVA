package br.com.fiap.agroorbit.services;

import br.com.fiap.agroorbit.dtos.request.SatelliteDataRequest;
import br.com.fiap.agroorbit.dtos.response.SatelliteDataResponse;
import br.com.fiap.agroorbit.exceptions.ResourceNotFoundException;
import br.com.fiap.agroorbit.models.CropArea;
import br.com.fiap.agroorbit.models.SatelliteData;
import br.com.fiap.agroorbit.repositories.CropAreaRepository;
import br.com.fiap.agroorbit.repositories.SatelliteDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SatelliteDataService {

    private final SatelliteDataRepository repository;
    private final CropAreaRepository cropAreaRepository;

    @Cacheable(value = "satelliteData")
    public Page<SatelliteDataResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(SatelliteDataResponse::fromEntity);
    }

    @Cacheable(value = "satelliteDataByCropArea", key = "#cropAreaId")
    public List<SatelliteDataResponse> findByCropArea(Long cropAreaId) {
        return repository.findByCropAreaId(cropAreaId).stream().map(SatelliteDataResponse::fromEntity).toList();
    }

    public Optional<SatelliteData> findLatestEntityByCropArea(Long cropAreaId) {
        return repository.findTopByCropAreaIdOrderByCaptureDateDescCreatedAtDesc(cropAreaId);
    }

    @Cacheable(value = "satelliteDatum", key = "#id")
    public SatelliteDataResponse findById(Long id) {
        return repository.findById(id)
                .map(SatelliteDataResponse::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Dado satelital não encontrado"));
    }

    @CacheEvict(value = {"satelliteData", "satelliteDataByCropArea", "satelliteDatum", "dashboard"}, allEntries = true)
    public SatelliteDataResponse create(SatelliteDataRequest request) {
        CropArea cropArea = cropAreaRepository.findById(request.cropAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Talhão não encontrado"));
        return SatelliteDataResponse.fromEntity(repository.save(new SatelliteData(request, cropArea)));
    }

    @CacheEvict(value = {"satelliteData", "satelliteDataByCropArea", "satelliteDatum", "dashboard"}, allEntries = true)
    public SatelliteDataResponse update(Long id, SatelliteDataRequest request) {
        SatelliteData satelliteData = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dado satelital não encontrado"));
        CropArea cropArea = cropAreaRepository.findById(request.cropAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Talhão não encontrado"));
        satelliteData.updateFrom(request, cropArea);
        return SatelliteDataResponse.fromEntity(repository.save(satelliteData));
    }

    @CacheEvict(value = {"satelliteData", "satelliteDataByCropArea", "satelliteDatum", "dashboard"}, allEntries = true)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Dado satelital não encontrado");
        }
        repository.deleteById(id);
    }
}
