package com.example.login5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MyAdapter extends FirebaseRecyclerAdapter<Filtering, MyAdapter.myViewHolder> {
    public MyAdapter(@NonNull FirebaseRecyclerOptions<Filtering> options){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Filtering filtering) {

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

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameView);
            mbti = itemView.findViewById(R.id.mbtiView);
        }
    }

}
