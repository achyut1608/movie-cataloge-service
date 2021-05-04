package com.learn.micrsoservice.moviecatalogeservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString
public class Movie {

    private String movieId;
    private String movieName;
    private String movieDescription;
    private String genre;
    private String releaseYear;



}
