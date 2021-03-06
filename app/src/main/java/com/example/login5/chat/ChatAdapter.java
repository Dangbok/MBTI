package com.example.login5.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login5.MemoryData;
import com.example.login5.R;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    //RecyclerView Adapter

    private List<ChatList> chatLists;
    private final Context context;
    private String userMobile;

    public ChatAdapter(List<ChatList> chatLists, Context context) {
        this.chatLists = chatLists;
        this.context = context;
        this.userMobile = MemoryData.getData(context);
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    } //레이아웃 생성

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        //뷰홀더가 재활용될 때, 실행되는 메서드
        int safePosition = holder.getAbsoluteAdapterPosition();
        ChatList list2 = chatLists.get(safePosition);

        if(list2.getMobile().equals(userMobile)){
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);

            holder.myMessage.setText(list2.getMessage());
            holder.myTime.setText(list2.getTime());
        }
        else{
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);

            holder.oppoMessage.setText(list2.getMessage());
            holder.oppoTime.setText(list2.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }
    //아이템 개수를 조회

    public void updateChatList(List<ChatList> chatLists){
        this.chatLists = chatLists;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout oppoLayout, myLayout;
        private TextView oppoMessage, myMessage;
        private TextView oppoTime, myTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            oppoMessage = itemView.findViewById(R.id.oppoMessage);
            myMessage = itemView.findViewById(R.id.myMessage);
            oppoTime = itemView.findViewById(R.id.oppoMsgTime);
            myTime = itemView.findViewById(R.id.myMsgTime);
        }
    }
}