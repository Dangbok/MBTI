package com.example.login5;

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

public class MyAdapter extends FirebaseRecyclerAdapter<Filtering, MyAdapter.myViewHolder>  {
    public MyAdapter(@NonNull FirebaseRecyclerOptions<Filtering> options){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Filtering filtering) {

        //profile 추가
        Glide.with(holder.itemView)
                .load(filtering.getProfile())
                .placeholder(R.mipmap.ic_launcher_round)
                .centerCrop()
                .error(R.mipmap.ic_launcher_round)
                .into(holder.profile);

        holder.name.setText(filtering.getName());
        holder.mbti.setText(filtering.getMbti());

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView name, mbti;

        //profile 추가
        ImageView profile;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameView);
            mbti = itemView.findViewById(R.id.mbtiView);

            //profile 추가
            profile = itemView.findViewById(R.id.imageView);
        }
    }

}
