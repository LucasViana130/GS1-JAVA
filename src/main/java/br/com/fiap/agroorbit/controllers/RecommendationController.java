package br.com.fiap.agroorbit.controllers;

import br.com.fiap.agroorbit.dtos.request.RecommendationRequest;
import br.com.fiap.agroorbit.dtos.response.RecommendationResponse;
import br.com.fiap.agroorbit.services.RecommendationService;
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
@RequestMapping("/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recomendação", description = "Endpoints para gerenciamento de recomendações")
public class RecommendationController {

    private final RecommendationService service;

    @GetMapping
    @Operation(summary = "Listar recomendações com paginação")
    public ResponseEntity<Page<RecommendationResponse>> findAll(@PageableDefault(size = 10) @ParameterObject Pageable pageable) {
        Page<RecommendationResponse> recommendations = service.findAll(pageable);
        recommendations.forEach(recommendation -> recommendation.add(linkTo(methodOn(RecommendationController.class).findById(recommendation.getId())).withSelfRel()));
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar recomendação por ID")
    public ResponseEntity<RecommendationResponse> findById(@PathVariable Long id) {
        RecommendationResponse recommendation = service.findById(id);
        recommendation.add(linkTo(methodOn(RecommendationController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(recommendation);
    }

    @PostMapping
    @Operation(summary = "Cadastrar recomendação")
    public ResponseEntity<RecommendationResponse> create(@RequestBody @Valid RecommendationRequest request) {
        RecommendationResponse recommendation = service.create(request);
        recommendation.add(linkTo(methodOn(RecommendationController.class).findById(recommendation.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(recommendation);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar recomendação")
    public ResponseEntity<RecommendationResponse> update(@PathVariable Long id, @RequestBody @Valid RecommendationRequest request) {
        RecommendationResponse recommendation = service.update(id, request);
        recommendation.add(linkTo(methodOn(RecommendationController.class).findById(id)).withSelfRel());
        return ResponseEntity.ok(recommendation);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir recomendação")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/alert/{alertId}")
    @Operation(summary = "Listar recomendações por alerta")
    public ResponseEntity<List<RecommendationResponse>> findByAlert(@PathVariable Long alertId) {
        return ResponseEntity.ok(service.findByAlert(alertId));
    }
}
