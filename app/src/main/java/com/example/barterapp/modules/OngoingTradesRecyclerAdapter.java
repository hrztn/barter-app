package com.example.barterapp.modules;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barterapp.R;
import com.example.barterapp.TradeDetailsActivity;
import com.example.barterapp.model.TradeModelClass;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;

public class OngoingTradesRecyclerAdapter extends RecyclerView.Adapter<OngoingTradesRecyclerAdapter.ViewHolder> {
    public static SimpleDateFormat sdateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Context mContext ;
    private LayoutInflater mLayoutInflater;
    private ArrayList<TradeModelClass> trades=new ArrayList<>();
    public OngoingTradesRecyclerAdapter(Context context,ArrayList<TradeModelClass> trades ) {
        this.mContext=context;
        this.trades=trades;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.view_trade_row, parent, false);
        return new OngoingTradesRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TradeModelClass t=trades.get(position);
        holder.ItemOName.setText(t.getItemTwo().getName());
        holder.ItemRName.setText(t.getItemOne().getName());
        {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(t.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            Date d = cal.getTime();
            date.setTimeZone(TimeZone.getTimeZone("GMT"));
            String localTime = date.format(d);
            if (t.isCanceled()){
                holder.dateTv.setText("Cancelled on " + localTime);
                holder.stutesTv.setText("Cancelled");
                holder.stutesTv.setTextColor(GRAY);

            }else{
                if (t.isCompletedByuserOne() && t.isCompletedByuserTwo()) {
                    holder.dateTv.setText("Completed on " + localTime);
                    holder.stutesTv.setText("Completed");
                    holder.stutesTv.setTextColor(GREEN);
                } else {
                    holder.dateTv.setText("Started on " + localTime);
                    holder.stutesTv.setText("InProgress");
                }
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(mContext, TradeDetailsActivity.class);
                intent.putExtra("trade", (Serializable) t);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trades.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView uName,ItemRName,ItemOName,stutesTv,dateTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.tuserTrader);
            uName=itemView.findViewById(R.id.tm_textView_username);
            ItemRName=itemView.findViewById(R.id.tmrequestItemName);
            ItemOName=itemView.findViewById(R.id.tmoffereditem_name);
            stutesTv=itemView.findViewById(R.id.StutstextView);
            dateTv=itemView.findViewById(R.id.DatetextView);
        }
    }


}
