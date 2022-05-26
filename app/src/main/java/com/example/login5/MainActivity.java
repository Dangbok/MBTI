package com.example.login5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login5.chat.Chat;
import com.example.login5.chat.LinearLayoutManagerWrapper;
import com.example.login5.messages.Messages;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(this,  LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions<Filtering> options =
                new FirebaseRecyclerOptions.Builder<Filtering>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), Filtering.class)
                        .build();

        //채팅하기 클릭시 Chat 액티비티 이동
        myAdapter = new MyAdapter(options, position -> {
            Intent intent = new Intent(this, Chat.class);
            intent.putExtra("myId", getIntent().getStringExtra("userId"));
            startActivity(intent);
        });

        recyclerView.setAdapter(myAdapter);

    }

    @Override
    protected void onStart(){
        super.onStart();
        myAdapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        myAdapter.stopListening();
    }

    //옵션바 search 클릭시 - 검색필터 구현
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setQueryHint("MBTI 검색");


        //searchView 검색어 입력/검색 이벤트 처리
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //검색 버튼이 눌러졌을 때 이벤트 처리 (검색어 완료시)
            public boolean onQueryTextSubmit(String newText) {
                mysearch(newText);
                return false;
            }

            @Override
            //검색어가 변경되었을 때 이벤트 처리 (검색어 입력시)
            public boolean onQueryTextChange(String newText) {
                mysearch(newText);
                return false;
            }
        });
        return  super.onCreateOptionsMenu(menu);
    }

    //옵션바 채팅리스트 클릭시 Messages 액티비티 이동
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.chatList) {
            Intent intent = new Intent(this, Messages.class);
            intent.putExtra("userId", getIntent().getStringExtra("userId"));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private  void mysearch(String str){

        FirebaseRecyclerOptions<Filtering> options =
                new FirebaseRecyclerOptions.Builder<Filtering>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("mbti").startAt(str).endAt(str+"\uf8ff"), Filtering.class)
                        .build();

        myAdapter = new MyAdapter(options, position -> {
            Intent intent = new Intent(this, Chat.class);
            intent.putExtra("myId", getIntent().getStringExtra("userId"));
            startActivity(intent);
        });

        myAdapter.startListening();
        recyclerView.setAdapter(myAdapter);
    }
}