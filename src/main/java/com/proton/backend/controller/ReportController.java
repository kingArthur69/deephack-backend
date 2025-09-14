package com.proton.backend.controller;

import com.proton.backend.dto.ReportDTO;
import com.proton.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/raport")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ReportDTO> getReport(
            @RequestParam(name = "meterId") Long meterId,
            @RequestParam(name = "direction") String direction,
            @RequestParam(name = "interval") String interval,
            @RequestParam(name = "startDate") String startDate) {
        try {
            return ResponseEntity.ok(reportService.getReport(meterId, direction, interval, startDate));
        } catch (Exception e) {
            log.error("Error while getting report", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/predict")
    public ResponseEntity<String> getReportForecast(
            @RequestParam(name = "meterId") Long meterId,
            @RequestParam(name = "direction") String direction,
            @RequestParam(name = "interval") String interval) {
        try {
            return ResponseEntity.ok(reportService.getReportForecast(meterId, direction, interval));
        } catch (Exception e) {
            log.error("Error while getting report", e);
            return ResponseEntity.badRequest().build();
        }
    }


}
