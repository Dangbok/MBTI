package com.example.login5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MyAdapter extends FirebaseRecyclerAdapter<Filtering, MyAdapter.myViewHolder> {
    public MyAdapter(@NonNull FirebaseRecyclerOptions<Filtering> options, OnButtonClickListener onButtonClickListener){
        super(options);
        this.onButtonClickListener = onButtonClickListener;
    }

    public interface OnButtonClickListener {
        void onButtonClick(int position);
    }

    private OnButtonClickListener onButtonClickListener;

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
        return new myViewHolder(view, onButtonClickListener);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView profile;
        TextView name, mbti;
        Button chat;

        public myViewHolder(@NonNull View itemView,
                            OnButtonClickListener itemClickListener) {
            super(itemView);

            profile = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.nameView);
            mbti = itemView.findViewById(R.id.mbtiView);
            chat = itemView.findViewById(R.id.chat_button);

            chat.setOnClickListener(view -> {
                final int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    itemClickListener.onButtonClick(position);
                }
            });
        }
    }

}
