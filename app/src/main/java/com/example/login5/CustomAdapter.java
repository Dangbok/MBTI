package com.example.login5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        CustomViewHolder holder=new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.iv_profile);
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_age.setText(String.valueOf(arrayList.get(position).getAge()));
        holder.tv_mbti.setText(arrayList.get(position).getMbti());
        holder.tv_myself.setText(arrayList.get(position).getMyself());

    }

    @Override
    public int getItemCount() {
        return (arrayList !=null ? arrayList.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        TextView tv_name,tv_age,tv_mbti,tv_myself;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile=itemView.findViewById(R.id.profile);
            this.tv_name=itemView.findViewById(R.id.tv_name);
            this.tv_age=itemView.findViewById(R.id.tv_age);
            this.tv_mbti=itemView.findViewById(R.id.tv_mbti);
            this.tv_myself=itemView.findViewById(R.id.tv_myself);
        }
    }
}



