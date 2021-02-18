package com.example.kiitanonchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class myAdapter extends FirebaseRecyclerAdapter<model, myAdapter.myViewHolder> {
    String url, username;

    public myAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull model model) {
        username = holder.user.getContext().getSharedPreferences("Current_Username", 0).getString("username", "left");
        url = model.getImageUrl();
        holder.user.setText(model.getUser() + ":");
        if (!url.equals("$null$")) {
            //image
            holder.imageView.setVisibility(View.VISIBLE);
            holder.chats.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.timeImg.setText(model.getTime());
            holder.timeImg.setVisibility(View.VISIBLE);
            Glide.with(holder.user.getContext().getApplicationContext())
                    .load(url)
                    .into(holder.imageView);
        } else {
            //no image
            holder.chats.setVisibility(View.VISIBLE);
            holder.chats.setText(model.getChats() + "          ");
            holder.time.setVisibility(View.VISIBLE);
            holder.timeImg.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
            holder.time.setText(model.getTime());
        }

        holder.chats.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String chat_id = getRef(position).getKey();
                if (username.equals(model.getUser()) && !model.getChats().equals("[message deleted]")) {
                    Snackbar snackbar = Snackbar.make(v, "Confirm Delete?", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Delete", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            String path = "/AllChat/" + chat_id;
                            DatabaseReference chatRef = db.getReference(path);
                            model.setChats("[message deleted]");
                            chatRef.setValue(model);
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
                return true;
            }
        });

        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String chat_id = getRef(position).getKey();
                if (username.equals(model.getUser())) {
                    Snackbar snackbar = Snackbar.make(v, "Confirm Delete?", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Delete", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            String path = "/AllChat/" + chat_id;
                            DatabaseReference chatRef = db.getReference(path);
                            model.setChats("[message deleted]");
                            model.setImageUrl("$null$");
                            chatRef.setValue(model);
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
                return true;
            }
        });


//        Set the username color in the chat
        if(model.getUser().equals("admin")){
            holder.user.setTextColor(holder.user.getContext().getResources().getColor(R.color.red));
        }
        else{
        String uColor = model.getUserColor();
        switch (uColor) {
            case "R.color.text5":
                holder.user.setTextColor(holder.user.getContext().getResources().getColor(R.color.text5));
                break;
            case "R.color.text1":
                holder.user.setTextColor(holder.user.getContext().getResources().getColor(R.color.text1));
                break;
            case "R.color.text2":
                holder.user.setTextColor(holder.user.getContext().getResources().getColor(R.color.text2));
                break;
            case "R.color.text3":
                holder.user.setTextColor(holder.user.getContext().getResources().getColor(R.color.text3));
                break;
            case "R.color.text4":
                holder.user.setTextColor(holder.user.getContext().getResources().getColor(R.color.text4));
                break;
        }}
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        TextView chats, time, timeImg;
        TextView user;
        ImageView imageView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
//            linearLayout = itemView.findViewById(R.id.frame);
            time = itemView.findViewById(R.id.time);
            timeImg = itemView.findViewById(R.id.timeImg);
            user = itemView.findViewById(R.id.user);
            chats = itemView.findViewById(R.id.chats);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
