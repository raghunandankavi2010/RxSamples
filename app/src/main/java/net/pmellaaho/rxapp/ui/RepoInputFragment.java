package net.pmellaaho.rxapp.ui;

import android.app.Activity;
import android.graphics.Movie;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.squareup.leakcanary.RefWatcher;

import net.pmellaaho.rxapp.R;
import net.pmellaaho.rxapp.RxApp;
import net.pmellaaho.rxapp.model.Contributor;
import net.pmellaaho.rxapp.model.ContributorsModel;
import net.pmellaaho.rxapp.model.Movies;
import net.pmellaaho.rxapp.model.SpotifyMoviesModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.google.common.base.Strings.isNullOrEmpty;

public class RepoInputFragment extends Fragment {
    /*public static final String OWNER = "square";
    private static final String REPO = "retrofit";*/
    private static final String REQUEST_PENDING = "requestPending";
    private static final String STATE_MOVIES = "state_movies";

    private ProgressBar mProgress;
    private View mErrorText;
    public int pageCount=1,totalcount=12448;

    private EmptyRecyclerView mRecyclerView;

    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    //ContributorsModel mModel;
    SpotifyMoviesModel mModel;
    private boolean mRequestPending;
    private ImageGridAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private ArrayList<Movies> mList = new ArrayList<>();

    
    public RepoInputFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("unsubscribe");

        mSubscriptions.unsubscribe();

        RefWatcher refWatcher = RxApp.getRefWatcher();
        refWatcher.watch(this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movielist, container, false);
        mProgress = (ProgressBar) root.findViewById(R.id.progressBar);
        mErrorText = root.findViewById(R.id.list_empty);
        mRecyclerView = (EmptyRecyclerView) root.findViewById(R.id.recyclerView);

        mAdapter = new ImageGridAdapter();
        mRecyclerView.setAdapter(mAdapter);
        //mGridLayoutManager = new GridLayoutManager(getActivity(),2);
        //mRecyclerView.setLayoutManager(mRecyclerView.getManager());
        mModel = ((RxApp)getActivity().getApplication()).component().spotifymoviesModel();

        if(savedInstanceState != null) {
            mList = savedInstanceState.getParcelableArrayList(STATE_MOVIES);
            mAdapter.addPosts(mList);
        }else
        {
            fetchData();
        }
        mRecyclerView.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int current_page, int totalItemCount) {
                //add progress item
                Log.i("Count is", "" + current_page);
                pageCount = current_page;
                //mquestionsArrayList.add(null);
                //mAdapter.notifyItemInserted(mquestionsArrayList.size());
                if (totalcount != totalItemCount) {
                    mSubscriptions.add(
                            mModel.getMoviesList("vote_average.desc", current_page)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new MoviesListSubscriber()));
                } else {

                }

            }
        });


    /*    mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                int spanCount = mGridLayoutManager.getSpanCount();
                return (mMoviesAdapter.isLoadMore(position) *//**//* && (position % spanCount == 0) *//**//*) ? spanCount : 1;
            }
        });

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mMoviesAdapter);*/






        return root;
    }
    
    private void fetchData() {
        Timber.d("Fetch data from RepoInputFragment");
        mRequestPending = true;
        mProgress.setVisibility(View.VISIBLE);
        mErrorText.setVisibility(View.INVISIBLE);

        mSubscriptions.add(
                mModel.getMoviesList("vote_average.desc", 1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new MoviesListSubscriber()));
    }


/*    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(REQUEST_PENDING, mRequestPending);
    }*/

/*    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getBoolean(REQUEST_PENDING, false)) {

            if (mModel.getRequest() != null) {
                Timber.d("Subscribe to pending request");
                mSubscriptions.add(
                        mModel.getRequest()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new MoviesListSubscriber()));
            }
        }
    }*/

    private class MoviesListSubscriber extends Subscriber<ArrayList<Movies>> {

        @Override
        public void onNext(ArrayList<Movies> movies) {
            Timber.d("received data from model");
            mProgress.setVisibility(View.INVISIBLE);
            for(Movies movie:movies) {
                Log.i("RepoInputFragment", "" + movie.getTitle());
            }
            mList.addAll(movies);
            mAdapter.addPosts(movies);
        }

        @Override
        public void onCompleted() {
            Timber.d("request completed");
            mRequestPending = false;


        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "request failed");
            mProgress.setVisibility(View.INVISIBLE);
            mErrorText.setVisibility(View.VISIBLE);
            mModel.reset();
        }
    }
    

    
    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIES, mAdapter.getmList());

    }
}
