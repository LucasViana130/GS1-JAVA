package br.com.fiap.agroorbit.controllers;

import br.com.fiap.agroorbit.dtos.request.CropAreaRequest;
import br.com.fiap.agroorbit.dtos.response.*;
import br.com.fiap.agroorbit.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@RestController
@RequestMapping("/crop-areas")
@RequiredArgsConstructor
@Tag(name = "Talhão", description = "Endpoints para gerenciamento de talhões")
public class CropAreaController {

    private final CropAreaService service;
    private final SensorService sensorService;
    private final SensorReadingService sensorReadingService;
    private final SatelliteDataService satelliteDataService;
    private final ClimateAlertService climateAlertService;
    private final RecommendationService recommendationService;

    @GetMapping
    @Operation(summary = "Listar todos os talhões com paginação")
    public ResponseEntity<Page<CropAreaResponse>> findAll(@PageableDefault(size = 10) @ParameterObject Pageable pageable) {
        Page<CropAreaResponse> cropAreas = service.findAll(pageable);
        cropAreas.forEach(cropArea -> cropArea.add(linkTo(methodOn(CropAreaController.class).findById(cropArea.getId())).withSelfRel()));
        return ResponseEntity.ok(cropAreas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar talhão por ID")
    public ResponseEntity<CropAreaResponse> findById(@PathVariable Long id) {
        CropAreaResponse cropArea = service.findById(id);
        cropArea.add(linkTo(methodOn(CropAreaController.class).findById(id)).withSelfRel());
        cropArea.add(linkTo(methodOn(CropAreaController.class).findSensors(id)).withRel("sensors"));
        cropArea.add(linkTo(methodOn(CropAreaController.class).findReadings(id)).withRel("readings"));
        cropArea.add(linkTo(methodOn(CropAreaController.class).findSatelliteData(id)).withRel("satellite-data"));
        cropArea.add(linkTo(methodOn(CropAreaController.class).findAlerts(id)).withRel("alerts"));
        cropArea.add(linkTo(methodOn(CropAreaController.class).findRecommendations(id)).withRel("recommendations"));
        return ResponseEntity.ok(cropArea);
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo talhão")
    public ResponseEntity<CropAreaResponse> create(@RequestBody @Valid CropAreaRequest request) {
        CropAreaResponse cropArea = service.create(request);
        cropArea.add(linkTo(methodOn(CropAreaController.class).findById(cropArea.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(cropArea);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar talhão")
    public ResponseEntity<CropAreaResponse> update(@PathVariable Long id, @RequestBody @Valid CropAreaRequest request) {
        CropAreaResponse cropArea = service.update(id, request);
        cropArea.add(linkTo(methodOn(CropAreaController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(cropArea);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir talhão")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/sensors")
    public ResponseEntity<List<SensorResponse>> findSensors(@PathVariable Long id) {
        return ResponseEntity.ok(sensorService.findByCropArea(id));
    }

    @GetMapping("/{id}/readings")
    public ResponseEntity<List<SensorReadingResponse>> findReadings(@PathVariable Long id) {
        return ResponseEntity.ok(sensorReadingService.findByCropArea(id));
    }

    @GetMapping("/{id}/satellite-data")
    public ResponseEntity<List<SatelliteDataResponse>> findSatelliteData(@PathVariable Long id) {
        return ResponseEntity.ok(satelliteDataService.findByCropArea(id));
    }

    @GetMapping("/{id}/alerts")
    public ResponseEntity<List<ClimateAlertResponse>> findAlerts(@PathVariable Long id) {
        return ResponseEntity.ok(climateAlertService.findByCropArea(id));
    }

    @GetMapping("/{id}/recommendations")
    public ResponseEntity<List<RecommendationResponse>> findRecommendations(@PathVariable Long id) {
        return ResponseEntity.ok(recommendationService.findByCropArea(id));
    }
}
