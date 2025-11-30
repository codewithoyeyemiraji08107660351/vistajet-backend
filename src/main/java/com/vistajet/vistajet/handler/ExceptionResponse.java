package com.vistajet.vistajet.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    private Integer code;
    private String businessErrorDescription;
    private Set<String> validationError;
    private String error;
    private Map<String, String> errors;
}

