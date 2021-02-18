package com.example.kiitanonchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class ConfessAdapter extends FirebaseRecyclerAdapter<modelc, ConfessAdapter.myViewHolder> {
    public ConfessAdapter(@NonNull FirebaseRecyclerOptions<modelc> optionsc) {
        super(optionsc);
    }


    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull modelc modelc) {
//        username = holder.user.getContext().getSharedPreferences("Current_Username", 0).getString("username", "left");
            holder.ll.setVisibility(View.VISIBLE);
            holder.user.setText(modelc.getUser() + ":");
            holder.chats.setText(modelc.getChats());
            holder.time.setText(modelc.getTime());
//          Set the username color in the chat
            String uColor = modelc.getUserColor();
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
        }
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowconf, parent, false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        TextView chats, time;
        TextView user;
        LinearLayout ll;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
//            linearLayout = itemView.findViewById(R.id.frame);
            time = itemView.findViewById(R.id.timec);
            user = itemView.findViewById(R.id.userc);
            chats = itemView.findViewById(R.id.chatsc);
            ll = itemView.findViewById(R.id.ll);
        }
    }
}

