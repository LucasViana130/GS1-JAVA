package br.com.fiap.agroorbit.controllers;

import br.com.fiap.agroorbit.dtos.response.RiskAnalysisResponse;
import br.com.fiap.agroorbit.services.RiskAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/risk-analysis")
@RequiredArgsConstructor
public class RiskAnalysisController {

    private final RiskAnalysisService riskAnalysisService;

    @PostMapping("/{cropAreaId}")
    public ResponseEntity<RiskAnalysisResponse> analyze(@PathVariable Long cropAreaId) {
        return ResponseEntity.ok(riskAnalysisService.analyze(cropAreaId));
    }
}
