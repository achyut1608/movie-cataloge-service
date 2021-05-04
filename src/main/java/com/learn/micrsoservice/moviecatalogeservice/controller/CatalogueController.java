package com.learn.micrsoservice.moviecatalogeservice.controller;

import com.learn.micrsoservice.moviecatalogeservice.model.CatalogueItem;
import com.learn.micrsoservice.moviecatalogeservice.service.CatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/movie-catalogue")
public class CatalogueController {

    @Autowired
    CatalogueService catalogueService;

    @GetMapping("/")
    public ResponseEntity<List<CatalogueItem>>  getAllCatalogue(){
        try {
            System.out.println("inside get all catalogue controller");
            return new ResponseEntity<List<CatalogueItem>>(catalogueService.getAllCatalogueItem(),HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is some problem ");
        }
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<CatalogueItem> getCatalogueById(@PathVariable("movieId") String movieId){
        try {
            System.out.println("inside get catalogue by id controller");
            return new ResponseEntity<CatalogueItem>(catalogueService.getSpecificMovieRatings(movieId), HttpStatus.OK);
        } catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is some problem");
        }
    }


    @DeleteMapping("/{movieId}")
    public void deleteCatalogue(@PathVariable("movieId") String movieId) {
        try {
            catalogueService.deleteMovieRatings(movieId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @PostMapping
    public ResponseEntity<CatalogueItem> insertCatalogueItem(@RequestBody CatalogueItem item){
        try {
            return new ResponseEntity<CatalogueItem>(catalogueService.insertCatalogue(item),HttpStatus.OK);
        } catch (Exception ex){

            ex.printStackTrace();
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is some problem");
        }

    }

}
