package com.proton.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Entity
@Table(name = "meters", uniqueConstraints = {@UniqueConstraint(columnNames = "serial_number")})
public class Meter {

    @Id
    @Column(name = "meter_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_number")
    private String serialNumber;
    private Double voltage;
    private Double power;
    private Double current;
    @Column(name = "power_factor")
    private Double powerFactor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
