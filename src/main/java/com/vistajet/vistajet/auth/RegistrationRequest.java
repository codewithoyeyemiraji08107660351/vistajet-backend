package com.vistajet.vistajet.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "First name cannot be empty")
    @NotBlank(message = "First name cannot be blank")
    private String  firstname;
    @NotEmpty(message = "Last name cannot be empty")
    @NotBlank(message = "Last name cannot be blank")
    private String lastname;
    @Email(message = "Email is not valid")
    @NotEmpty(message = "Email name cannot be empty")
    @NotBlank(message = "Email name cannot be blank")
    @Column(unique = true)
    private String email;
    @Size(min = 5, message = "Password should be 5 character minimum")
    private String password;
    private String role;
    @NotEmpty(message = "Phone number cannot be empty")
    @NotBlank(message = "Phone number cannot be blank")
    private String phone_no;
    @NotEmpty(message = "Address cannot be empty")
    @NotBlank(message = "Address cannot be blank")
    private String address;
}
