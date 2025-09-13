package com.proton.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proton.backend.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    @NotNull
    private Long contractNumber;
    @NotEmpty
    @JsonProperty("IDNP")
    private String idnp;
    @NotEmpty
    private String buletin;
    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
}
