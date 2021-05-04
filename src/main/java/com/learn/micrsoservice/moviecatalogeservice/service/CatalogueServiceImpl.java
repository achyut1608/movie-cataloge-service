package com.learn.micrsoservice.moviecatalogeservice.service;

import com.learn.micrsoservice.moviecatalogeservice.model.CatalogueItem;
import com.learn.micrsoservice.moviecatalogeservice.model.Movie;
import com.learn.micrsoservice.moviecatalogeservice.model.Ratings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:application.properties")
public class CatalogueServiceImpl implements  CatalogueService{


    @Value("${moviesURL}")
    private String moviesURL;

    @Value("${ratingsURL}")
    private String ratingsURL;

    WebClient moviesWebClient;
    WebClient ratingsWebClient;


    public List<CatalogueItem> getAllCatalogueItem(){

        System.out.println("in get all catalogue item service");

        moviesWebClient = WebClient.create(moviesURL);
        ratingsWebClient = WebClient.create(ratingsURL);

        List<CatalogueItem> listCatalogue = new ArrayList<>();

        Flux<Movie> movieFlux = moviesWebClient.get().retrieve().bodyToFlux(Movie.class);
        List<Movie> movieList = movieFlux.collectList().block();
        System.out.println("movie list :"+movieList);
        Flux<Ratings> ratingsFlux = ratingsWebClient.get().retrieve().bodyToFlux(Ratings.class);
        List<Ratings> ratingList = ratingsFlux.collectList().block();
        System.out.println("rating List : "+ratingList);
        for(int i=0;i<movieList.size();i++){
            Movie movie = movieList.get(i);
            Ratings ratings = ratingList.get(i);

            CatalogueItem catalogueItem = createCatalogueItem(movie,ratings);

            listCatalogue.add(catalogueItem);

        }
        System.out.println("list catalogue : "+listCatalogue);
        return listCatalogue;

    }


    public CatalogueItem getSpecificMovieRatings(String movieId){

        System.out.println("movie URL : " + moviesURL);
        System.out.println("ratings URL : " + ratingsURL);


        moviesWebClient = WebClient.create(moviesURL+"/"+movieId);
        ratingsWebClient = WebClient.create(ratingsURL+movieId);

        Mono<Movie> movieMono = moviesWebClient.get().retrieve().bodyToMono(Movie.class);
        Movie movie = movieMono.block();

        Mono<Ratings> ratingsMono = ratingsWebClient.get().retrieve().bodyToMono(Ratings.class);
        Ratings ratings = ratingsMono.block();

        CatalogueItem catalogueItem = createCatalogueItem(movie,ratings);

        return catalogueItem;

    }

    @Override
    public CatalogueItem insertCatalogue(CatalogueItem catalogueItem){

        CatalogueItem item1 = null;
        Movie movie = new Movie();
        movie.setMovieId(catalogueItem.getMovieID());
        movie.setMovieName(catalogueItem.getMovieName());
        movie.setGenre(catalogueItem.getGenre());
        movie.setReleaseYear(catalogueItem.getReleaseYear());
        movie.setMovieDescription(catalogueItem.getDescription());

        Ratings ratings = new Ratings();
        ratings.setMovieId(catalogueItem.getMovieID());
        ratings.setMovieRatings(catalogueItem.getRatings());

        moviesWebClient = WebClient.create(moviesURL);
        Mono<Movie> mono = moviesWebClient.post().body(Mono.just(movie),Movie.class).retrieve().bodyToMono(Movie.class);

        Movie movie1 = mono.block();

        ratingsWebClient = WebClient.create(ratingsURL);
        Mono<Ratings> monoRatings = ratingsWebClient.post().body(Mono.just(ratings),Ratings.class).retrieve().bodyToMono(Ratings.class);
        Ratings ratings1 = monoRatings.block();

        CatalogueItem catalogueItem1 = createCatalogueItem(movie1,ratings1);
        return  catalogueItem1;
    }

    @Override
    public void deleteMovieRatings(String movieId) {
        try{
            moviesWebClient = WebClient.create(moviesURL+"/"+movieId);
            ratingsWebClient = WebClient.create(ratingsURL+movieId);

            Mono<Void> movieMono = moviesWebClient.delete().retrieve().bodyToMono(void.class);
            movieMono.block();

            Mono<Void> ratingsMono = ratingsWebClient.delete().retrieve().bodyToMono(Void.class);
            ratingsMono.block();

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private CatalogueItem createCatalogueItem(Movie movie, Ratings ratings){
        CatalogueItem catalogueItem = new CatalogueItem();
        catalogueItem.setMovieID(movie.getMovieId());
        catalogueItem.setMovieName(movie.getMovieName());
        catalogueItem.setDescription(movie.getMovieDescription());
        catalogueItem.setGenre(movie.getGenre());
        catalogueItem.setReleaseYear(movie.getReleaseYear());
        catalogueItem.setRatings(ratings.getMovieRatings());
        return catalogueItem;
    }
}
