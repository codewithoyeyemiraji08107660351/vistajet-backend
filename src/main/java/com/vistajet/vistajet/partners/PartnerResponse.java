package com.vistajet.vistajet.partners;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PartnerResponse {
    private Integer id;
    private String companyName;
    private String companyLogo;
}
