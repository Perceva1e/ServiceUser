package com.example.userservice.model;

import lombok.Data;

@Data
public class Film {
    private Long id;
    private String title;
    private int releaseYear;
    private String originalLanguage;
    private Integer duration;
    private Double rating;
}
