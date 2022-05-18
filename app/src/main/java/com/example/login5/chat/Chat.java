package com.example.login5.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login5.MemoryData;
import com.example.login5.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    //AppCompatActivity -> App Bar/Action Bar 지원

    private final List<ChatList> chatLists = new ArrayList<>();
    //List 생성
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mbti-matching-users-default-rtdb.firebaseio.com/");
    //Realtime Database에서 Data를 읽어옴 -> getInstance()필요 -> getReferenceFromUrl로 참조 변수 생성
    String getUserMobile = ""; //getUserMobile이라는 문자열을 빈 문자열로 초기화
    private String chatKey;
    private RecyclerView chattingRecyclerView;
    private ChatAdapter chatAdapter;
    //변수 선언
    private boolean loadingFirstTime = true; //boolean 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //onCreate -> call back method로 생명 주기에서 생성 단계에 한 번 실행되는 method
        super.onCreate(savedInstanceState); //activity 실행 및 기록 -> setContentView() 호출
        setContentView(R.layout.activity_chat); //activity_chat.xml

        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView nameTV = findViewById(R.id.name);
        final EditText messageEditText = findViewById(R.id.messageEditTxt);
        final CircleImageView profilePic = findViewById(R.id.profilePic);
        final ImageView sendBtn = findViewById(R.id.sendBtn);
        //id값을 이용하여 특정 뷰를 받아오고 활용하여 변수 선언

        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);
        //chattingRecyclerView = .activity_chat 채팅방

        final String getName = getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getMobile = getIntent().getStringExtra("mobile");
        // get data from messages adapter class

        getUserMobile = MemoryData.getData(Chat.this);
        // get user mobile from memory

        nameTV.setText(getName);
        Picasso.get().load(getProfilePic).into(profilePic);
        //.load(경로).into(받아온 이미지를 사용할 공간)

        chattingRecyclerView.setHasFixedSize(true); //setHasFixedSize = 크기 변환 방지
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));
        //Chat에 LayoutManager 생성

        chatAdapter = new ChatAdapter(chatLists, Chat.this);
        //ChatAdapter 생성

        chattingRecyclerView.setAdapter(chatAdapter);
        //chattingRecyclerView(채팅방)에 chatAdapter 적용

        databaseReference.addValueEventListener(new ValueEventListener() {
            //Firebase Database에서 데이터를 읽어옴
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //onDataChange() = 경로의 전체 내용을 읽고 변경사항을 수신 대기함
                //DataSnapshot = 메소드가 호출될 때의 Data (data를 꺼내옴)

                if (chatKey.isEmpty()) {
                    // generate chat key. by default chatKey is 1
                    chatKey = "1";

                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                        //String.valueOf() = 파라미터가 null이면 문자열 "null"을 만들어서 반환

                    }
                }

                if(snapshot.hasChild("chat")){
                    //자식 노드 chat이 있다면

                    if(snapshot.child("chat").child(chatKey).hasChild("messages")){
                        //chat>chatKey>messages가 있다면

                        chatLists.clear(); //chatLists의 모든 요소 제거

                        for(DataSnapshot messagesSnapshot : snapshot.child("chat").child(chatKey).child("messages").getChildren()){
                            //
                            if(messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("mobile")){

                                final String messageTimestamps = messagesSnapshot.getKey();
                                final String getMobile = messagesSnapshot.child("mobile").getValue(String.class);
                                final String getMsg = messagesSnapshot.child("msg").getValue(String.class);

                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps)); //String을 Long으로 변환
                                Date date = new Date();
                                Log.d("DATE", date.toString());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a", Locale.KOREAN);
                                Log.d("DATE", simpleTimeFormat.format(date).toString());
                                //Timestamp

                                ChatList chatList = new ChatList(getMobile, getName, getMsg, simpleTimeFormat.format(date));

                                chatLists.add(chatList);

                                if(loadingFirstTime || Long.parseLong(messageTimestamps) > Long.parseLong(MemoryData.getLastMsgTS(Chat.this, chatKey))){

                                    loadingFirstTime = false;

                                    MemoryData.saveLastMsgTS(messageTimestamps, chatKey, Chat.this);
                                    chatAdapter.updateChatList(chatLists);

                                    chattingRecyclerView.scrollToPosition(chatLists.size() - 1);

                                }
                            }

                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String getTxtMessage = messageEditText.getText().toString();

                // get current timestamps
                final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);


                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserMobile);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getMobile);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(getTxtMessage);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("mobile").setValue(getUserMobile);

                // clear edit text
                messageEditText.setText("");

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
