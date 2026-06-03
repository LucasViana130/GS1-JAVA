package br.com.fiap.agroorbit.services;

import br.com.fiap.agroorbit.dtos.request.SensorRequest;
import br.com.fiap.agroorbit.dtos.response.SensorResponse;
import br.com.fiap.agroorbit.exceptions.ResourceNotFoundException;
import br.com.fiap.agroorbit.models.*;
import br.com.fiap.agroorbit.models.enums.SensorType;
import br.com.fiap.agroorbit.repositories.CropAreaRepository;
import br.com.fiap.agroorbit.repositories.SensorReadingRepository;
import br.com.fiap.agroorbit.repositories.SensorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository repository;
    private final CropAreaRepository cropAreaRepository;
    private final SensorReadingRepository sensorReadingRepository;

    @Cacheable(value = "sensors")
    public Page<SensorResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(SensorResponse::fromEntity);
    }

    @Cacheable(value = "sensorsByCropArea", key = "#cropAreaId")
    public List<SensorResponse> findByCropArea(Long cropAreaId) {
        return repository.findByCropAreaId(cropAreaId).stream().map(SensorResponse::fromEntity).toList();
    }

    public Sensor findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor não encontrado"));
    }

    @Cacheable(value = "sensor", key = "#id")
    public SensorResponse findById(Long id) {
        return SensorResponse.fromEntity(findEntityById(id));
    }

    @CacheEvict(value = {"sensors", "sensorsByCropArea", "dashboard"}, allEntries = true)
    public SensorResponse create(SensorRequest request) {
        CropArea cropArea = cropAreaRepository.findById(request.cropAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Talhão não encontrado"));
        return SensorResponse.fromEntity(repository.save(createSensorByType(request, cropArea)));
    }

    @CacheEvict(value = {"sensors", "sensorsByCropArea", "sensor", "dashboard"}, allEntries = true)
    public SensorResponse update(Long id, SensorRequest request) {
        Sensor sensor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor não encontrado"));
        CropArea cropArea = cropAreaRepository.findById(request.cropAreaId())
                .orElseThrow(() -> new ResourceNotFoundException("Talhão não encontrado"));
        sensor.updateFrom(request, cropArea);
        return SensorResponse.fromEntity(repository.save(sensor));
    }

    @Transactional
    @CacheEvict(value = {
            "sensors",
            "sensor-readings",
            "crop-areas",
            "dashboard"
    }, allEntries = true)
    public void delete(Long id) {
        Sensor sensor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor não encontrado"));

        sensorReadingRepository.deleteBySensorId(sensor.getId());

        repository.delete(sensor);
    }

    private Sensor createSensorByType(SensorRequest request, CropArea cropArea) {
        if (request.sensorType() == SensorType.SOIL_MOISTURE) {
            return new SoilMoistureSensor(request, cropArea);
        }
        if (request.sensorType() == SensorType.WEATHER_STATION) {
            return new WeatherStationSensor(request, cropArea);
        }
        if (request.sensorType() == SensorType.TEMPERATURE) {
            return new TemperatureSensor(request, cropArea);
        }
        return new HumiditySensor(request, cropArea);
    }
}
