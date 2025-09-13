package com.proton.backend.controller;

import com.proton.backend.dto.MeterDTO;
import com.proton.backend.dto.UserDTO;
import com.proton.backend.mapper.MeterMapper;
import com.proton.backend.mapper.UserMapper;
import com.proton.backend.model.Meter;
import com.proton.backend.service.MeterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meters")
public class MeterController {

    private final MeterService meterService;

    @GetMapping("/{id}")
    ResponseEntity<List<MeterDTO>> findMeters(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(meterService.findAllByUserId(id).stream().map(MeterMapper::toMeterDTO).toList());
        } catch (Exception e) {
            log.error("Error while getting user", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    ResponseEntity<Long> addMeter(@RequestBody MeterDTO meterDTO) {
        try {
            return ResponseEntity.ok(meterService.save(MeterMapper.fromMeterDTO(meterDTO)).getId());
        } catch (Exception e) {
            log.error("Error while getting user", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
