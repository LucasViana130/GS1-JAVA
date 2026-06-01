package br.com.fiap.agroorbit.controllers;

import br.com.fiap.agroorbit.dtos.request.ClimateAlertRequest;
import br.com.fiap.agroorbit.dtos.request.ClimateAlertStatusRequest;
import br.com.fiap.agroorbit.dtos.response.ClimateAlertResponse;
import br.com.fiap.agroorbit.services.ClimateAlertService;
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
@RequestMapping("/climate-alerts")
@RequiredArgsConstructor
@Tag(name = "Alerta Climático", description = "Endpoints para gerenciamento de alertas climáticos")
public class ClimateAlertController {

    private final ClimateAlertService service;

    @GetMapping
    @Operation(summary = "Listar alertas com paginação")
    public ResponseEntity<Page<ClimateAlertResponse>> findAll(@PageableDefault(size = 10) @ParameterObject Pageable pageable) {
        Page<ClimateAlertResponse> alerts = service.findAll(pageable);
        alerts.forEach(alert -> alert.add(linkTo(methodOn(ClimateAlertController.class).findById(alert.getId())).withSelfRel()));
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alerta por ID")
    public ResponseEntity<ClimateAlertResponse> findById(@PathVariable Long id) {
        ClimateAlertResponse alert = service.findById(id);
        alert.add(linkTo(methodOn(ClimateAlertController.class).findById(id)).withSelfRel());
        alert.add(linkTo(methodOn(ClimateAlertController.class).resolve(id)).withRel("resolve"));
        return ResponseEntity.ok(alert);
    }

    @GetMapping("/open")
    @Operation(summary = "Listar alertas abertos")
    public ResponseEntity<List<ClimateAlertResponse>> findOpen() {
        return ResponseEntity.ok(service.findOpen());
    }

    @GetMapping("/critical")
    @Operation(summary = "Listar alertas críticos")
    public ResponseEntity<List<ClimateAlertResponse>> findCritical() {
        return ResponseEntity.ok(service.findCritical());
    }

    @GetMapping("/crop-area/{cropAreaId}")
    @Operation(summary = "Listar alertas por talhão")
    public ResponseEntity<List<ClimateAlertResponse>> findByCropArea(@PathVariable Long cropAreaId) {
        return ResponseEntity.ok(service.findByCropArea(cropAreaId));
    }

    @PostMapping
    @Operation(summary = "Cadastrar alerta")
    public ResponseEntity<ClimateAlertResponse> create(@RequestBody @Valid ClimateAlertRequest request) {
        ClimateAlertResponse alert = service.create(request);
        alert.add(linkTo(methodOn(ClimateAlertController.class).findById(alert.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(alert);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status do alerta")
    public ResponseEntity<ClimateAlertResponse> updateStatus(@PathVariable Long id, @RequestBody @Valid ClimateAlertStatusRequest request) {
        ClimateAlertResponse alert = service.updateStatus(id, request);
        alert.add(linkTo(methodOn(ClimateAlertController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(alert);
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Marcar alerta como resolvido")
    public ResponseEntity<ClimateAlertResponse> resolve(@PathVariable Long id) {
        ClimateAlertResponse alert = service.resolve(id);
        alert.add(linkTo(methodOn(ClimateAlertController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(alert);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir alerta")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
