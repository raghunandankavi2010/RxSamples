package net.pmellaaho.rxapp.ui;

import android.content.Intent;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import net.pmellaaho.rxapp.R;
import net.pmellaaho.rxapp.Utils;
import net.pmellaaho.rxapp.model.Movies;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Raghunandan on 23-09-2015.
 */

// Thanks to Chiuki Chan's AutoFitRecyclerView. I picked that square island blog post.
public class ImageGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ArrayList<Movies> mList;
    private SortedList<Movies> mSortedList;


    public ImageGridAdapter() {
        this.mList = new ArrayList<>();
    }

    public void addPosts(ArrayList<Movies> newPosts) {
        mList.addAll(newPosts);
        notifyDataSetChanged();
    }

    public static ArrayList<Movies> getmList() {
        return mList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        RecyclerView.ViewHolder  viewHolder;
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_item, parent, false);

        viewHolder = new ViewHolder_item(v);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, final int position) {

        if(vh instanceof ViewHolder_item) {

            ViewHolder_item viewHolder = (ViewHolder_item) vh;
            Picasso.with(viewHolder.m_Image.getContext()).
                    load(Utils.BASE_IMAGE_URL+
                            viewHolder.m_Image.getContext().getResources().getString(R.string.phone_size)+
                            mList.get(position).getPoster_path())
                    .into(viewHolder.m_Image);
        }
    }



    // inner class to hold a reference to each item of RecyclerView
    public  static class ViewHolder_item extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView m_Image;

        public ViewHolder_item(View itemLayoutView) {
            super(itemLayoutView);
            m_Image = (ImageView) itemLayoutView.findViewById(R.id.imageView);
            m_Image.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            /*int pos = getAdapterPosition();
            Movies movie = mList.get(pos);
            Intent intent =  new Intent(view.getContext(),DetailsActivity.class);
            intent.putExtra("movie",movie);
            view.getContext().startActivity(intent);*/
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mList.size();
    }



}
