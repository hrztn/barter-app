package com.example.barterapp.modules;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barterapp.R;
import com.example.barterapp.model.MessageModelClass;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {
    ArrayList<MessageModelClass> messages = new ArrayList<>();
    Context context;
    private LayoutInflater mInflater;

    public ChatRecyclerViewAdapter(Context context, ArrayList<MessageModelClass> messagesList) {
        this.mInflater = LayoutInflater.from(context);
        this.messages=messagesList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.message_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageModelClass message=messages.get(position);

        holder.messageTv.setText(message.getMessage());
        holder.timeTv.setText(getSendtimeString(message.getSendtime()));

        if(message.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.RIGHT;

            holder.messageTv.setLayoutParams(params);
            holder.timeTv.setLayoutParams(params);
            holder.messagelayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.msg_receiver));
        }

    }


    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTv,timeTv;
        LinearLayout messagelayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTv=itemView.findViewById(R.id.messagetextView);
            timeTv=itemView.findViewById(R.id.timetextView);
            messagelayout=itemView.findViewById(R.id.messagelayout);

        }
    }
    public String getSendtimeString(String postedTime) {
        String stime = null;

        CharSequence ago="";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long time = sdf.parse(postedTime).getTime();
            long now = System.currentTimeMillis();

            ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ago.toString();

    }

}
