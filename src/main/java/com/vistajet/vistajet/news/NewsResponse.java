package com.vistajet.vistajet.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponse {
    private Integer id;
    private String title;
    private String content;
    private String imageUrl;
    private String author;
    private LocalDateTime createdAt;
}
