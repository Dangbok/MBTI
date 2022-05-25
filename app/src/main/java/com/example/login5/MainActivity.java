package com.example.login5;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;


import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recview);  //아이디 연결
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Filtering> options =
                new FirebaseRecyclerOptions.Builder<Filtering>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), Filtering.class)
                        .build();

        myAdapter = new MyAdapter(options);
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

    //search - 검색필터 구현
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        //추가
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
    private  void mysearch(String str){

        FirebaseRecyclerOptions<Filtering> options =
                new FirebaseRecyclerOptions.Builder<Filtering>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("mbti").startAt(str).endAt(str+"\uf8ff"), Filtering.class)
                        .build();

        myAdapter = new MyAdapter(options);
        myAdapter.startListening();
        recyclerView.setAdapter(myAdapter);
    }

}