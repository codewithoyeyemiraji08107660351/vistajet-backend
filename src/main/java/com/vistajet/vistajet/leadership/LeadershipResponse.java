package com.vistajet.vistajet.leadership;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeadershipResponse {

    private Integer id;
    private String fullName;
    private String position;
    private String bio;
    private String image;
}
