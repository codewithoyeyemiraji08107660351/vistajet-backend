package com.vistajet.vistajet.news;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewsRequest {

    Integer id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "News content cannot be blank")
    private String content;

    @NotBlank(message = "News content cannot be blank")
    private String author;
    private String image;
}
