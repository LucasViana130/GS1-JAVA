package br.com.fiap.agroorbit.controllers;

import br.com.fiap.agroorbit.dtos.request.FarmRequest;
import br.com.fiap.agroorbit.dtos.response.CropAreaResponse;
import br.com.fiap.agroorbit.dtos.response.FarmResponse;
import br.com.fiap.agroorbit.services.CropAreaService;
import br.com.fiap.agroorbit.services.FarmService;
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
@RequestMapping("/farms")
@RequiredArgsConstructor
@Tag(name = "Fazenda", description = "Endpoints para gerenciamento de fazendas")
public class FarmController {

    private final FarmService service;
    private final CropAreaService cropAreaService;

    @GetMapping
    @Operation(summary = "Listar todas as fazendas com paginação")
    public ResponseEntity<Page<FarmResponse>> findAll(@PageableDefault(size = 10) @ParameterObject Pageable pageable) {
        Page<FarmResponse> farms = service.findAll(pageable);
        farms.forEach(farm -> farm.add(linkTo(methodOn(FarmController.class).findById(farm.getId())).withSelfRel()));
        return ResponseEntity.ok(farms);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fazenda por ID")
    public ResponseEntity<FarmResponse> findById(@PathVariable Long id) {
        FarmResponse farm = service.findById(id);
        farm.add(linkTo(methodOn(FarmController.class).findById(id)).withSelfRel());
        farm.add(linkTo(methodOn(FarmController.class).findCropAreas(id)).withRel("crop-areas"));
        return ResponseEntity.ok(farm);
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova fazenda")
    public ResponseEntity<FarmResponse> create(@RequestBody @Valid FarmRequest request) {
        FarmResponse farm = service.create(request);
        farm.add(linkTo(methodOn(FarmController.class).findById(farm.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(farm);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar fazenda")
    public ResponseEntity<FarmResponse> update(@PathVariable Long id, @RequestBody @Valid FarmRequest request) {
        FarmResponse farm = service.update(id, request);
        farm.add(linkTo(methodOn(FarmController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(farm);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir fazenda")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/crop-areas")
    @Operation(summary = "Listar talhões de uma fazenda")
    public ResponseEntity<List<CropAreaResponse>> findCropAreas(@PathVariable Long id) {
        return ResponseEntity.ok(cropAreaService.findByFarm(id));
    }
}
