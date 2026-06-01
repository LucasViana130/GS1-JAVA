package br.com.fiap.agroorbit.services;

import br.com.fiap.agroorbit.dtos.request.SensorReadingRequest;
import br.com.fiap.agroorbit.dtos.response.SensorReadingResponse;
import br.com.fiap.agroorbit.exceptions.ResourceNotFoundException;
import br.com.fiap.agroorbit.models.Sensor;
import br.com.fiap.agroorbit.models.SensorReading;
import br.com.fiap.agroorbit.repositories.SensorReadingRepository;
import br.com.fiap.agroorbit.repositories.SensorRepository;
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
public class SensorReadingService {

    private final SensorReadingRepository repository;
    private final SensorRepository sensorRepository;

    @Cacheable(value = "sensorReadings")
    public Page<SensorReadingResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(SensorReadingResponse::fromEntity);
    }

    @Cacheable(value = "sensorReadingsBySensor", key = "#sensorId")
    public List<SensorReadingResponse> findBySensor(Long sensorId) {
        return repository.findBySensorId(sensorId).stream().map(SensorReadingResponse::fromEntity).toList();
    }

    @Cacheable(value = "sensorReadingsByCropArea", key = "#cropAreaId")
    public List<SensorReadingResponse> findByCropArea(Long cropAreaId) {
        return repository.findBySensorCropAreaId(cropAreaId).stream().map(SensorReadingResponse::fromEntity).toList();
    }

    public Optional<SensorReading> findLatestEntityByCropArea(Long cropAreaId) {
        return repository.findTopBySensorCropAreaIdOrderByReadingDateDesc(cropAreaId);
    }

    @Cacheable(value = "sensorReading", key = "#id")
    public SensorReadingResponse findById(Long id) {
        return repository.findById(id)
                .map(SensorReadingResponse::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Leitura de sensor não encontrada"));
    }

    @CacheEvict(value = {"sensorReadings", "sensorReadingsBySensor", "sensorReadingsByCropArea", "sensorReading", "dashboard"}, allEntries = true)
    public SensorReadingResponse create(SensorReadingRequest request) {
        Sensor sensor = sensorRepository.findById(request.sensorId())
                .orElseThrow(() -> new ResourceNotFoundException("Sensor não encontrado"));
        return SensorReadingResponse.fromEntity(repository.save(new SensorReading(request, sensor)));
    }

    @CacheEvict(value = {"sensorReadings", "sensorReadingsBySensor", "sensorReadingsByCropArea", "sensorReading", "dashboard"}, allEntries = true)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Leitura de sensor não encontrada");
        }
        repository.deleteById(id);
    }
}
