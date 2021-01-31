package com.example.kiitanonchat;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class myAdapter extends FirebaseRecyclerAdapter<model, myAdapter.myViewHolder>
{



    public myAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull model model) {
        holder.chats.setText(model.getChats() + "          ");
        holder.user.setText(model.getUser() + ":");
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
        holder.time.setText(model.getTime());
        String username = holder.user.getContext().getSharedPreferences("Current_Username" , 0).getString("username" , "left");
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

    static class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView chats, time;
        LinearLayout LinearLayout;
        TextView user;
        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);
            LinearLayout = itemView.findViewById(R.id.LinearLayout);
            time = itemView.findViewById(R.id.time);
            user = itemView.findViewById(R.id.user);
            chats= itemView.findViewById(R.id.tv);
        }
    }
}
