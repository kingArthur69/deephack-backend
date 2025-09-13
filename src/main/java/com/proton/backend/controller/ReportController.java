package com.proton.backend.controller;

import com.proton.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/raport")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

//    @GetMapping
//    public ResponseEntity<Report> getReport() {
//
//    }


}
