package br.com.fiap.agroorbit.controllers;

import br.com.fiap.agroorbit.dtos.request.SensorReadingRequest;
import br.com.fiap.agroorbit.dtos.response.SensorReadingResponse;
import br.com.fiap.agroorbit.services.SensorReadingService;
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
@RequestMapping("/sensor-readings")
@RequiredArgsConstructor
@Tag(name = "Leitura de Sensor", description = "Endpoints para gerenciamento de leituras simuladas")
public class SensorReadingController {

    private final SensorReadingService service;

    @GetMapping
    @Operation(summary = "Listar leituras com paginação")
    public ResponseEntity<Page<SensorReadingResponse>> findAll(@PageableDefault(size = 10) @ParameterObject Pageable pageable) {
        Page<SensorReadingResponse> readings = service.findAll(pageable);
        readings.forEach(reading -> reading.add(linkTo(methodOn(SensorReadingController.class).findById(reading.getId())).withSelfRel()));
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar leitura por ID")
    public ResponseEntity<SensorReadingResponse> findById(@PathVariable Long id) {
        SensorReadingResponse reading = service.findById(id);
        reading.add(linkTo(methodOn(SensorReadingController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(reading);
    }

    @PostMapping
    @Operation(summary = "Cadastrar leitura simulada")
    public ResponseEntity<SensorReadingResponse> create(@RequestBody @Valid SensorReadingRequest request) {
        SensorReadingResponse reading = service.create(request);
        reading.add(linkTo(methodOn(SensorReadingController.class).findById(reading.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(reading);
    }

    @GetMapping("/sensor/{sensorId}")
    @Operation(summary = "Listar leituras por sensor")
    public ResponseEntity<List<SensorReadingResponse>> findBySensor(@PathVariable Long sensorId) {
        return ResponseEntity.ok(service.findBySensor(sensorId));
    }

    @GetMapping("/crop-area/{cropAreaId}")
    @Operation(summary = "Listar leituras por talhão")
    public ResponseEntity<List<SensorReadingResponse>> findByCropArea(@PathVariable Long cropAreaId) {
        return ResponseEntity.ok(service.findByCropArea(cropAreaId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir leitura")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
