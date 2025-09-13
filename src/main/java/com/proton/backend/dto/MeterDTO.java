package com.proton.backend.dto;

import com.proton.backend.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class MeterDTO {

    private Long id;
    private String serialNumber;
    private Double voltage;
    private Double power;
    private Double current;
    private Double powerFactor;
    private User user;

}
