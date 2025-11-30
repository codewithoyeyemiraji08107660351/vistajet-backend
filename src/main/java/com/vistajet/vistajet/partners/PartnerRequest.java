package com.vistajet.vistajet.partners;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PartnerRequest {

    Integer id;

    @NotBlank(message = "Company name cannot be blank")
    private String companyName;

    private String companyLogo;
}
