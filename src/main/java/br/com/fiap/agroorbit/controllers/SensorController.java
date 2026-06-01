package br.com.fiap.agroorbit.controllers;

import br.com.fiap.agroorbit.dtos.request.SensorRequest;
import br.com.fiap.agroorbit.dtos.response.SensorReadingResponse;
import br.com.fiap.agroorbit.dtos.response.SensorResponse;
import br.com.fiap.agroorbit.services.SensorReadingService;
import br.com.fiap.agroorbit.services.SensorService;
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
@RequestMapping("/sensors")
@RequiredArgsConstructor
@Tag(name = "Sensor", description = "Endpoints para gerenciamento de sensores")
public class SensorController {

    private final SensorService service;
    private final SensorReadingService sensorReadingService;

    @GetMapping
    @Operation(summary = "Listar sensores com paginação")
    public ResponseEntity<Page<SensorResponse>> findAll(@PageableDefault(size = 10) @ParameterObject Pageable pageable) {
        Page<SensorResponse> sensors = service.findAll(pageable);
        sensors.forEach(sensor -> sensor.add(linkTo(methodOn(SensorController.class).findById(sensor.getId())).withSelfRel()));
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sensor por ID")
    public ResponseEntity<SensorResponse> findById(@PathVariable Long id) {
        SensorResponse sensor = service.findById(id);
        sensor.add(linkTo(methodOn(SensorController.class).findById(id)).withSelfRel());
        sensor.add(linkTo(methodOn(SensorController.class).findReadings(id)).withRel("readings"));
        return ResponseEntity.ok(sensor);
    }

    @PostMapping
    @Operation(summary = "Cadastrar sensor")
    public ResponseEntity<SensorResponse> create(@RequestBody @Valid SensorRequest request) {
        SensorResponse sensor = service.create(request);
        sensor.add(linkTo(methodOn(SensorController.class).findById(sensor.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(sensor);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sensor")
    public ResponseEntity<SensorResponse> update(@PathVariable Long id, @RequestBody @Valid SensorRequest request) {
        SensorResponse sensor = service.update(id, request);
        sensor.add(linkTo(methodOn(SensorController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(sensor);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir sensor")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/readings")
    @Operation(summary = "Listar leituras do sensor")
    public ResponseEntity<List<SensorReadingResponse>> findReadings(@PathVariable Long id) {
        return ResponseEntity.ok(sensorReadingService.findBySensor(id));
    }
}
