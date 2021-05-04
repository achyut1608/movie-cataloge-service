package com.learn.micrsoservice.moviecatalogeservice.service;


import com.learn.micrsoservice.moviecatalogeservice.model.CatalogueItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CatalogueService {

    public List<CatalogueItem> getAllCatalogueItem();
    public CatalogueItem getSpecificMovieRatings(String movieId);
    public void deleteMovieRatings(String movieId);
    public CatalogueItem insertCatalogue(CatalogueItem catalogueItem);
}
