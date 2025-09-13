package com.proton.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserService userService;
    private final MeterService meterService;
    private final InputDataService inputDataService;


}
