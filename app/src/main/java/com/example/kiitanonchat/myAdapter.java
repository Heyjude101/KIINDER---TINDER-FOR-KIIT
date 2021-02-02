package com.example.kiitanonchat;

import android.content.Context;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class myAdapter extends FirebaseRecyclerAdapter<model, com.example.kiitanonchat.myAdapter.myViewHolder>
    {
        String url;

        public myAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
            super(options);
        }


        @Override
        protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull model model) {

            String username = holder.user.getContext().getSharedPreferences("Current_Username" , 0).getString("username" , "left");
            Context context = holder.itemView.getContext();
            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                            String chat_id = getRef(position).getKey();
                            if(username.equals(model.getUser()))
                            {
                                Snackbar snackbar = Snackbar.make(v,"Confirm Delete?",Snackbar.LENGTH_LONG);
                                snackbar.setAction("Delete", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        String path = "/AllChat/" + context.getSharedPreferences("chatid" , 0).getString("chatid" , "test");
                                        DatabaseReference chatRef = db.getReference(path);
                                        chatRef.removeValue();
                                        context.getSharedPreferences("chatid" , 0).edit().clear().apply();
                                    }
                                });
                                snackbar.show();
                                context.getSharedPreferences("chatid" , 0).edit().putString("chatid" , chat_id).apply();
                            }
                            else{
                                context.getSharedPreferences("chatid" , 0).edit().clear().apply();
                            }
                    return true;
                }
            });
            url = model.getImageUrl();
            if(!url.equals("$null$")){
                holder.chats.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                holder.time.setVisibility(View.GONE);
                holder.timeImg.setVisibility(View.VISIBLE);
                Glide.with(holder.user.getContext().getApplicationContext())
                        .load(url)
                        .into(holder.imageView);
            }
            else{
                holder.chats.setVisibility(View.VISIBLE);
                holder.chats.setText(model.getChats() + "          ");
                holder.time.setVisibility(View.VISIBLE);
                holder.timeImg.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.GONE);
                holder.time.setText(model.getTime());
            }


            //Set the username in the message
            holder.user.setText(model.getUser() + ":");


            //Set the username color in the chat
            String uColor = model.getUserColor();
            if(uColor.equals("R.color.text5")){
                holder.user.setTextColor(holder.user.getContext().getResources().getColor(R.color.text5));
            }
            if(uColor.equals("R.color.text1")){
                holder.user.setTextColor(holder.user.getContext().getResources().getColor(R.color.text1));
            }
            if(uColor.equals("R.color.text2")){
                holder.user.setTextColor(holder.user.getContext().getResources().getColor(R.color.text2));
            }
            if(uColor.equals("R.color.text3")){
                holder.user.setTextColor(holder.user.getContext().getResources().getColor(R.color.text3));
            }
            if(uColor.equals("R.color.text4")){
                holder.user.setTextColor(holder.user.getContext().getResources().getColor(R.color.text4));
            }

            if(!username.isEmpty() && username.equals(model.getUser())){
                holder.LinearLayout.setGravity(Gravity.END);
            }
        }

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
            return new myViewHolder(view);
        }

        static class myViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            TextView chats, time, timeImg;
            android.widget.LinearLayout LinearLayout;
            TextView user;
            ImageView imageView;

            public myViewHolder(@NonNull View itemView) {
                super(itemView);
                LinearLayout = itemView.findViewById(R.id.LinearLayout);
                time = itemView.findViewById(R.id.time);
                timeImg = itemView.findViewById(R.id.timeImg);
                user = itemView.findViewById(R.id.user);
                chats = itemView.findViewById(R.id.chats);
                imageView = itemView.findViewById(R.id.imageView);
                imageView.setOnCreateContextMenuListener(this);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                Toast.makeText(imageView.getContext(), "context menu", Toast.LENGTH_SHORT).show();
                menu.add(this.getAdapterPosition(), 101, 0, "delete");

            }
        }}

