package com.vistajet.vistajet.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactRequest {

    private Integer id;
    @NotBlank(message = "Full name cannot be empty")
    private String fullName;

    @Email(message = "Email not valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Subject cannot be empty")
    private String subject;

    @NotBlank(message = "Message cannot be empty")
    private String message;

    @NotBlank(message = "Phone number cannot be cannot be empty")
    private String phoneNo;
}
