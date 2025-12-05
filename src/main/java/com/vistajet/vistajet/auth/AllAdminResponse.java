package com.vistajet.vistajet.auth;

import com.vistajet.vistajet.user.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllAdminResponse {

    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    private String address;
    private String phone_no;
}
