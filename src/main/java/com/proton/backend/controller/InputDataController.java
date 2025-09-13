package com.proton.backend.controller;

import com.proton.backend.service.InputDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class InputDataController {

    private final InputDataService inputDataService;

    @PostMapping("/upload")
    ResponseEntity<Object> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFile) {
        try {
            for (MultipartFile file : multipartFile) {
                inputDataService.save(file);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error uploading data files", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/uploadZip")
    ResponseEntity<Object> uploadZip(@RequestParam("files") List<MultipartFile> multipartFile) {
        try {
            for (MultipartFile file : multipartFile) {
                inputDataService.save(file);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error uploading data files", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
