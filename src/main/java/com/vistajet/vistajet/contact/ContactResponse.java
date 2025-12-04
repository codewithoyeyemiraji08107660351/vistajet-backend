package com.vistajet.vistajet.contact;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ContactResponse {

    private Integer id;
    private String fullName;
    private String email;
    private String subject;
    private String message;
    private String phoneNo;
    private LocalDateTime createdAt;
}
