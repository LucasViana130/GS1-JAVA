package br.com.fiap.agroorbit.controllers;

import br.com.fiap.agroorbit.dtos.request.SatelliteDataRequest;
import br.com.fiap.agroorbit.dtos.response.SatelliteDataResponse;
import br.com.fiap.agroorbit.services.SatelliteDataService;
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

@RestController
@RequestMapping("/satellite-data")
@RequiredArgsConstructor
@Tag(name = "Dado Satelital", description = "Endpoints para gerenciamento de dados satelitais")
public class SatelliteDataController {

    private final SatelliteDataService service;

    @GetMapping
    @Operation(summary = "Listar dados satelitais com paginação")
    public ResponseEntity<Page<SatelliteDataResponse>> findAll(@PageableDefault(size = 10) @ParameterObject Pageable pageable) {
        Page<SatelliteDataResponse> data = service.findAll(pageable);
        data.forEach(item -> item.add(linkTo(methodOn(SatelliteDataController.class).findById(item.getId())).withSelfRel()));
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar dado satelital por ID")
    public ResponseEntity<SatelliteDataResponse> findById(@PathVariable Long id) {
        SatelliteDataResponse data = service.findById(id);
        data.add(linkTo(methodOn(SatelliteDataController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(data);
    }

    @PostMapping
    @Operation(summary = "Cadastrar dado satelital")
    public ResponseEntity<SatelliteDataResponse> create(@RequestBody @Valid SatelliteDataRequest request) {
        SatelliteDataResponse data = service.create(request);
        data.add(linkTo(methodOn(SatelliteDataController.class).findById(data.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dado satelital")
    public ResponseEntity<SatelliteDataResponse> update(@PathVariable Long id, @RequestBody @Valid SatelliteDataRequest request) {
        SatelliteDataResponse data = service.update(id, request);
        data.add(linkTo(methodOn(SatelliteDataController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir dado satelital")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
