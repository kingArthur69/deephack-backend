package com.proton.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "input_data")
@CompoundIndexes({
        @CompoundIndex(name = "meter_date_idx", def = "{'meterId': 1, 'dateTime': 1}", unique = true)
})
public class InputData {

    public InputData() {
    }

    public InputData(Long meterId, LocalDateTime dateTime, Long energyImport, Long energyExport, Double transFullCoef) {
        this.meterId = meterId;
        this.dateTime = dateTime;
        this.energyImport = energyImport;
        this.energyExport = energyExport;
        this.transFullCoef = transFullCoef;
    }

    @Id
    private String id;

    private Long meterId;
    private LocalDateTime dateTime;
    private Long energyImport;
    private Long energyExport;
    private Double transFullCoef;
}
