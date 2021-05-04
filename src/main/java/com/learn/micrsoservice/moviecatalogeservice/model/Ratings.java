package com.learn.micrsoservice.moviecatalogeservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@ToString
public class Ratings {

    private String movieId;
    private String movieRatings;

}
