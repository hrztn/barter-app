package com.example.barterapp.modules;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.barterapp.ChatActivity;
import com.example.barterapp.PublicUserViewActivity;
import com.example.barterapp.R;
import com.example.barterapp.model.TradeItemModelClass;
import com.example.barterapp.model.TradeModelClass;
import com.example.barterapp.model.TradeReqModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class ManageRequestAdapter extends RecyclerView.Adapter<ManageRequestAdapter.ViewHolder> {

    private List<TradeReqModelClass> mData=new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private ItemServerAdapter.ItemClickListener mClickListener;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    public ManageRequestAdapter(Context context, List<TradeReqModelClass> data) {
        this.mInflater = LayoutInflater.from(context);
        Collections.reverse(data);
        this.mData=data;
        this.mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.manage_req_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TradeReqModelClass request = mData.get(position);
        TradeItemModelClass requestedItem=request.getRequestedItem();
        TradeItemModelClass offered_Item=request.getOffered_Item();
        holder.isAccepted=request.getAccepted();
        holder.requestId=request.getRequestId();
        holder.isRejected=request.getRejected();
        holder.rName.setText(request.getRequestedItem().getName());
        holder.rCond.setText("Condition : "+requestedItem.getCondition());
        holder.rQuantity.setText("Quantity : "+requestedItem.getQuantity());
        Picasso.get().load(requestedItem.getImageUrl()).into(holder.RitemImage);
        holder.oName.setText(offered_Item.getName());
        holder.oCond.setText("Condition : "+offered_Item.getCondition());
        holder.oQuantity.setText("Quantity : "+offered_Item.getQuantity());
        Picasso.get().load(offered_Item.getImageUrl()).into(holder.Offered_ItemImage);
        holder.messageET.setText(request.getMessage());
        if (request.getRequestedItem().getTraderID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.otherUser=request.getOffered_Item().getTraderID();
            Log.d("TAG", "onClick: "+holder.otherUser);
        }else{
            holder.otherUser=request.getRequestedItem().getTraderID();
            Log.d("TAG", "onClick: "+holder.otherUser);
        }
        {
            DatabaseReference rrr= FirebaseDatabase.getInstance().getReference().child("users").child(request.getOffered_Item().getTraderID());

            rrr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                    String imageUrl=""+map.get("userImage");
                    String uname=""+map.get("userName");
                    holder.userName.setText(uname);
                    Picasso.get().load(imageUrl).into(holder.userImage);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("TAG", "Failed to read value.", error.toException());
                }
            });
        }
        if(request.getOffered_Item().getTraderID().equals(user.getUid())){
            if(request.getAccepted()){
                holder.accept.setText("Accepted");
                holder.accept.setTextColor(Color.GREEN);
                holder.reject.setEnabled(false);
                holder.accept.setEnabled(false);
            }if(request.getRejected()){
                holder.reject.setText("Rejected");
                holder.reject.setTextColor(Color.RED);
                holder.accept.setEnabled(false);
                holder.reject.setEnabled(false);
            }
            if(request.getAccepted()||request.getRejected()){
                holder.reject.setEnabled(false);
                holder.accept.setEnabled(false);
            }else{
                holder.deleteButton.setVisibility(View.VISIBLE);
                holder.accept.setVisibility(GONE);
                holder.reject.setVisibility(GONE);}
        }else {
            if (request.getAccepted()) {
                holder.accept.setText("Accepted");
                holder.accept.setTextColor(Color.GREEN);
                holder.reject.setEnabled(false);
                holder.accept.setEnabled(false);
            }
            if (request.getRejected()) {
                holder.reject.setText("Rejected");
                holder.reject.setTextColor(Color.RED);
                holder.accept.setEnabled(false);
                holder.reject.setEnabled(false);
            }


        }
        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mContext, PublicUserViewActivity.class);
                i.putExtra("id",request.getOffered_Item().getTraderID());
                mContext.startActivity(i);
            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("requests").child(request.getRequestedItem().getTraderID());
                dataref.child(holder.requestId).child("accepted").setValue(true);
                DatabaseReference datarefSender = FirebaseDatabase.getInstance().getReference().child("requests").child(request.getOffered_Item().getTraderID());
                datarefSender.child(holder.requestId).child("accepted").setValue(true);
                holder.accept.setText("Accepted");
                holder. accept.setTextColor(Color.GREEN);
                holder. reject.setEnabled(false);
                holder. accept.setEnabled(false);
                createNewTrade(request);
            }   });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("requests").child(request.getRequestedItem().getTraderID());
                dataref.child(holder.requestId).child("rejected").setValue(true);
                DatabaseReference datarefSender = FirebaseDatabase.getInstance().getReference().child("requests").child(request.getOffered_Item().getTraderID());
                datarefSender.child(holder.requestId).child("rejected").setValue(true);



                holder. reject.setText("Rejected");
                holder. reject.setTextColor(Color.RED);
                holder.accept.setEnabled(false);
                holder.reject.setEnabled(false);
            }   });
        holder.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mContext, ChatActivity.class);
                i.putExtra("chatid",request.getChat_id());
                i.putExtra("otherId",holder.otherUser);
                Log.d("TAG", "onClick: "+holder.otherUser);
                mContext.startActivity(i);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRequestBYId(request.getRequestedItem().getTraderID(),holder.requestId);
                deleteRequestBYId(request.getOffered_Item().getTraderID(),holder.requestId);
                holder.deleteButton.setText("deleted");

                holder. deleteButton.setTextColor(Color.GRAY);
                holder. reject.setVisibility(GONE);
                holder. accept.setVisibility(GONE);
                notifyDataSetChanged();
            }   });
    }

    private void createNewTrade(TradeReqModelClass request) {
        TradeModelClass tradeModelClass=new TradeModelClass();
        tradeModelClass.setTradeID(""+System.currentTimeMillis());
        tradeModelClass.setUserOneId(request.getRequestedItem().getTraderID());
        tradeModelClass.setUserTwoId(request.getOffered_Item().getTraderID());
        tradeModelClass.setChat_id(request.getChat_id());
        tradeModelClass.setItemOne(request.getRequestedItem());
        tradeModelClass.setItemTwo(request.getOffered_Item());
        tradeModelClass.setCompletedByuserOne(false);
        tradeModelClass.setCompletedByuserTwo(false);
        tradeModelClass.setTime(Calendar.getInstance().getTime().toString());
        FirebaseDatabase fdb=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=fdb.getReference("trades");
        databaseReference.child(tradeModelClass.getTradeID()).setValue(tradeModelClass);
        Toast.makeText(mContext, "New Trade created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView RitemImage,Offered_ItemImage,userImage;
        TextView rName,oName,rCond,oCond,oQuantity,rQuantity,userName;
        TextView messageET;
        Button accept ;
        Button reject ;
        Button deleteButton ;
        Button chatButton ;
        boolean isAccepted;
        boolean isRejected;
        String requestId="";
        String otherUser;

        TradeReqModelClass mTradeReqModelClass=new TradeReqModelClass();
        ViewHolder(final View itemView) {
            super(itemView);
            final Context context=itemView.getContext();
            accept= (Button)itemView.findViewById(R.id.accept_btn);
            reject= (Button)itemView.findViewById(R.id.Reject_button);
            deleteButton= (Button)itemView.findViewById(R.id.deleterequest);
            RitemImage=itemView.findViewById(R.id.mrequestItemImageView);
            userImage=itemView.findViewById(R.id.m_user_imageView);
            userName=itemView.findViewById(R.id.m_textView_username);
            Offered_ItemImage=itemView.findViewById(R.id.mofferedItemImageView);
            rName=itemView.findViewById(R.id.mrequestItemName);
            oName=itemView.findViewById(R.id.moffereditem_name);
            rCond=itemView.findViewById(R.id.mrequested_condition_textViewD);
            oCond=itemView.findViewById(R.id.moffered_condition_textViewD);
            oQuantity=itemView.findViewById(R.id.moffered_quantity_textView_D);
            rQuantity=itemView.findViewById(R.id.mrequested_quantity_textView_D);
            messageET=itemView.findViewById(R.id.mrequestMessage);
            chatButton=itemView.findViewById(R.id.chatButton);


        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    TradeReqModelClass getItem(int pos) {
        return mData.get(pos);
    }

    public void deleteRequestBYId(String Uid, String RequestId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("requests").child(Uid);
        Query applesQuery = ref.orderByChild("requestId").equalTo(RequestId);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    if(appleSnapshot.getRef().removeValue().isSuccessful()){
                        Toast.makeText(mContext,"Deleted",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
    }

    void setClickListener(ManageRequestAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = (ItemServerAdapter.ItemClickListener) itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }

}
