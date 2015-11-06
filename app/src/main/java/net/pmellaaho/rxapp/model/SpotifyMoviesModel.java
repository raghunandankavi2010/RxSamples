package net.pmellaaho.rxapp.model;

/**
 * Created by Raghunandan on 04-11-2015.
 */

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.ResponseBody;

import net.pmellaaho.rxapp.network.GitHubApi;
import net.pmellaaho.rxapp.network.SpotifyApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.AsyncSubject;



@Singleton
public class SpotifyMoviesModel {

    private final SpotifyApi mApi;

    // Implement cache using An AsyncSubject which emits the last value
    // (and only the last value) emitted by the source Observable,
    // and only after that source Observable completes.
    private AsyncSubject<ArrayList<Movies>> mAsyncSubject;

    private String mSort;
    private int mPage;
    private ArrayList<Movies> mMoviesList = new ArrayList<>();
    private Observable<ArrayList<Movies>> observablelist;

    @Inject
    public SpotifyMoviesModel(SpotifyApi api) {
        mApi = api;
    }

    public void reset() {
        mAsyncSubject = null;
        mSort = null;
        mPage = -1;
    }

    public Observable<ArrayList<Movies>> getRequest() {
        return observablelist;
    }

    public Observable<ArrayList<Movies>> getMoviesList(final String sort_by, final int page) {


        return Observable.create(new Observable.OnSubscribe<ArrayList<Movies>>() {
            @Override
            public void call(final Subscriber<? super ArrayList<Movies>> subscriber) {


                Call<MoviesList> response =  mApi.getMoviesList(sort_by,page);
                response.enqueue(new Callback<MoviesList>() {
                    @Override
                    public void onResponse(Response<MoviesList> resp) {
                        // Get result Repo from response.body()

                        try {

                            ArrayList<Movies> mList = resp.body().getResults();
                            subscriber.onNext(mList);
                            //Log.i("SpotifyMoviesModel", "" + mList.get(0).getTitle());
                           // mMoviesList.addAll(mList);
                            //observablelist = Observable.just(mMoviesList);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

            /*    try {
                    NetworkResponse networkresponse = MusicApi.getApi().getMusic("Metallica");
                    subscriber.onNext(networkresponse);

                } catch(Exception e)
                {
                    subscriber.onError(e);
                }*/
            }
        });




       // }

//            return observablelist;
        }

}
