package com.example.barterapp.modules;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.barterapp.EditItemActivity;
import com.example.barterapp.R;
import com.example.barterapp.model.TradeItemModelClass;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyItemsRecyclerViewAdapter extends RecyclerView.Adapter<MyItemsRecyclerViewAdapter.ViewHolder>{
    private List<TradeItemModelClass> mData;
    private LayoutInflater mInflater;
    private List<TradeItemModelClass> exampleListFull = new ArrayList<>();
    private Context mContext;

    public MyItemsRecyclerViewAdapter(Context context, List<TradeItemModelClass> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public MyItemsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_view, parent, false);
        return new MyItemsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyItemsRecyclerViewAdapter.ViewHolder holder, int position) {
        TradeItemModelClass mTradeItemModelClass = mData.get(position);
        holder.mTradeItemModelClass = mTradeItemModelClass;
        holder.name.setText(mTradeItemModelClass.getName());
        Picasso.get().load(mTradeItemModelClass.getImageUrl()).into(holder.ImageView);


    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "getItemCount: " + mData.size());
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        android.widget.ImageView ImageView;

        TradeItemModelClass mTradeItemModelClass = new TradeItemModelClass();

        ViewHolder(final View itemView) {
            super(itemView);
            final Context context = itemView.getContext();
            name = itemView.findViewById(R.id.itemNameTv);
            ImageView = itemView.findViewById(R.id.item_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //sending intent extra with whole clicked object
                    Intent intent = new Intent(context, EditItemActivity.class);
                    intent.putExtra("item", (Serializable) mTradeItemModelClass);
                    context.startActivity(intent);
                }
            });
        }

    }

}
