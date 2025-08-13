package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    @GetMapping
    public ResponseEntity<?> getAnalysis() {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(analysisService.getAnalysis())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
