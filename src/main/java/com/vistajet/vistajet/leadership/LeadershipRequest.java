package com.vistajet.vistajet.leadership;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LeadershipRequest {

        private Integer id;

        @NotBlank(message = "Full name cannot be blank")
        private String fullName;

        @NotBlank(message = "Position cannot be blank")
        private String position;

        @NotBlank(message = "Bio cannot be blank")
        private String bio;

}
