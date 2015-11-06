package net.pmellaaho.rxapp.network;

import net.pmellaaho.rxapp.model.Contributor;
import net.pmellaaho.rxapp.model.Movies;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface GitHubApi {

    /**
     * See https://developer.github.com/v3/repos/#list-contributors
     * http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=6d32f2a6596004bb66069187b4c9b933
     */
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributors(@Path("owner") String owner,
                                               @Path("repo") String repo);
   /* @GET("discover/movie")
    Observable<List<Movies>> getMoviesList(  @Query("sort_by") String sort,
                                             @Query("page") int page);*/
}
