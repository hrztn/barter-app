package com.example.barterapp.modules;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.barterapp.ItemInfoActivity;
import com.example.barterapp.R;
import com.example.barterapp.model.TradeItemModelClass;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemServerAdapter extends RecyclerView.Adapter<ItemServerAdapter.ViewHolder> implements Filterable {
    private List<TradeItemModelClass> mData;
    private LayoutInflater mInflater;
    private List<TradeItemModelClass> exampleListFull=new ArrayList<>();
    private Context mContext;
    private ItemClickListener mClickListener;

    public ItemServerAdapter(Context context, List<TradeItemModelClass> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TradeItemModelClass mTradeItemModelClass = mData.get(position);
        holder.mTradeItemModelClass=mTradeItemModelClass;
        holder.name.setText(mTradeItemModelClass.getName());

        float rating=(float)(mTradeItemModelClass.getTotalStars()/mTradeItemModelClass.getTotal_reviews());
        holder.ratingBar_item.setRating(rating);
        Picasso.get().load(mTradeItemModelClass.getImageUrl()).into(holder.ImageView);


    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "getItemCount: "+mData.size());
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView name;
        android.widget.ImageView ImageView;
        RatingBar ratingBar_item;

        TradeItemModelClass mTradeItemModelClass=new TradeItemModelClass();
        ViewHolder(final View itemView) {
            super(itemView);
            final Context context=itemView.getContext();
            name = itemView.findViewById(R.id.itemNameTv);
            ratingBar_item = itemView.findViewById(R.id.ratingBar_item);
            ImageView = itemView.findViewById(R.id.item_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(context, ItemInfoActivity.class);
                    intent.putExtra("item", (Serializable) mTradeItemModelClass);
                    context.startActivity(intent);
                }
            });
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    TradeItemModelClass getItem(int pos) {
        return mData.get(pos);
    }

    void setClickListener(ItemServerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public Filter getFilter() {
        if(exampleListFull.size()==0){
            exampleListFull.addAll(mData);}
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<TradeItemModelClass> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TradeItemModelClass item : exampleListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getCategory().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData.clear();
            mData.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
