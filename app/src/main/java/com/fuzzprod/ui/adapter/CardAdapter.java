package com.fuzzprod.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuzzprod.R;
import com.fuzzprod.api.model.Item;
import com.fuzzprod.util.Log;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nirajan on 10/21/2015.
 */
public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{
    private static final String TAG = "CardAdapter";

    private static final int TEXT_VALUE = 0;
    private static final int IMAGE_VALUE = 1;


    public static final String TEXT_TYPE="text";
    public static final String IMAGE_TYPE="image";

    /**
     * Interface that implements on click listener for {@link RecyclerView}
     */
    public interface onItemClickListener{
        public void onItemClick(View view, int position);
    }

    // Click listener
    onItemClickListener mItemClickListener;

    public void setOnItemClickListener(final onItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    private List<Item> mItems;
    private List<Item> filteredData;

    public CardAdapter() {
        mItems = new ArrayList<Item>();
        filteredData = new ArrayList<Item>();
    }

    public void addData(Item item) {
        if(item != null && item.getData()!=null && !item.getData().isEmpty()) {
            mItems.add(item);
            filteredData.add(item);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Item getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        // here your custom logic to choose the view type
        Item item = getItem(position);
        switch (item.getType()) {
            case TEXT_TYPE:
                return TEXT_VALUE;
            case IMAGE_TYPE:
                return IMAGE_VALUE;
            default:
                return TEXT_VALUE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case IMAGE_VALUE:
                v =   LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_image, parent, false);
                viewHolder = new ImageViewHolder(v);
                break;
            case TEXT_VALUE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_text, parent, false);
                viewHolder = new TextViewHolder(v);
                break;
        }
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_card_item, parent, false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = filteredData.get(position);
        Log.d(TAG, "View Type: " + String.valueOf(getItemViewType(position)));
        switch (getItemViewType(position)) {
            case IMAGE_VALUE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                Picasso.with(imageViewHolder.imageView.getContext())
                        .load(item.getData())
                        .error(R.drawable.error)
                        .tag(imageViewHolder.imageView.getContext())
                        .into(imageViewHolder.imageView);
                break;
            case TEXT_VALUE:
                TextViewHolder textViewHolder = (TextViewHolder) holder;
                textViewHolder.itemTitle.setText(Html.fromHtml(item.getData()));
                break;
        }
    }

    public final class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.card_item_title)
        TextView itemTitle;


        @VisibleForTesting
        public TextView getItemTitle() {
            return itemTitle;
        }

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "item on click");
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public final class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.card_item_image)
        ImageView imageView;


        @VisibleForTesting
        public ImageView getImageView() {
            return imageView;
        }

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "item on click");
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d(TAG, "performFilter " + constraint);
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    Log.d(TAG, "Constraint null");
                    results.values = mItems;
                    results.count = mItems.size();
                } else {
                    Log.d(TAG, "Constraint NOT null");
                   List<Item> filterResultsData = new ArrayList<Item>();
                    for (Item item : mItems) {
                        if(item != null && item.getData()!=null && !item.getData().isEmpty()) {
                            if (item.getType().equals(constraint)) {
                                Log.d(TAG, "Found: " + item.getType());
                                filterResultsData.add(item);
                            }
                        }
                    }
                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData.clear();
                filteredData = (ArrayList<Item>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}