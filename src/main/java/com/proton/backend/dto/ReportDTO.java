package com.proton.backend.dto;

import java.time.Instant;
import java.util.List;

public record ReportDTO(Long meterId, List<Instant> dateTimes, List<Long> imports, List<Long> exports) {
}
