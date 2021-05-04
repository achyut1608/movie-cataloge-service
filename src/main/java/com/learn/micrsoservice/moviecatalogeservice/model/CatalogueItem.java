package com.learn.micrsoservice.moviecatalogeservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString
public class CatalogueItem {

    private String movieID;
    private String movieName;
    private String description;
    private String genre;
    private String releaseYear;
    private String ratings;

}
