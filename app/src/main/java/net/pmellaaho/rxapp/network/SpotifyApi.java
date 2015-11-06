package net.pmellaaho.rxapp.network;

import com.squareup.okhttp.ResponseBody;

import net.pmellaaho.rxapp.model.Movies;
import net.pmellaaho.rxapp.model.MoviesList;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Raghunandan on 04-11-2015.
 */
public interface SpotifyApi {

    //http://api.themoviedb.org/discover/movie?api_key=6d32f2a6596004bb66069187b4c9b933&sort_by=vote_average.desc&page=1
    //http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=6d32f2a6596004bb66069187b4c9b933&page=1
    @GET("discover/movie?api_key=6d32f2a6596004bb66069187b4c9b933")
    Call<MoviesList> getMoviesList(@Query("sort_by") String sort,
                                             @Query("page") int page);

}
