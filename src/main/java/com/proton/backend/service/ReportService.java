package com.proton.backend.service;

import com.proton.backend.model.InputData;
import com.proton.backend.dto.ReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    public static final double PEAK_BORDER = 0.98;
    private final InputDataService inputDataService;
    private final MLService mlService;

    public ReportDTO getReport(Long meterId, String direction, String interval, String startDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = start.plus(getInterval(interval));
        List<InputData> readings = inputDataService.findAllByMeterIdAndDateBetween(meterId, start, end);

        List<Instant> dateTimes = new ArrayList<>();
        List<Long> imports = new ArrayList<>();
        List<Long> exports = new ArrayList<>();

        for (int i = 1; i < readings.size(); i++) {
            InputData prev = readings.get(i - 1);
            InputData curr = readings.get(i);
            dateTimes.add(curr.getDateTime().toInstant(ZoneOffset.UTC));

            long consumption = curr.getEnergyImport() - prev.getEnergyImport();
            imports.add(consumption);

            long export = curr.getEnergyExport() - prev.getEnergyExport();
            exports.add(export);
        }

        return new ReportDTO(meterId, dateTimes, imports, exports);
    }

    private TemporalAmount getInterval(String interval) {
        return switch (interval) {
            case "month" -> Period.ofMonths(1);
            case "year" -> Period.ofYears(1);
            default -> Period.ofDays(1);
        };
    }

    public String getReportForecast(Long meterId, String direction, String interval) throws Exception {
        long hours = getInterval(interval).get(ChronoUnit.HOURS);
        return mlService.predict((int) hours, PEAK_BORDER).toString();
    }
}
