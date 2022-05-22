package com.example.login5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login5.chat.Chat;
import com.example.login5.chat.ChatList;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter myAdapter;


    public static ArrayList<Filtering> filterList = new ArrayList<Filtering>();

    private Button allButton, istjButton, istpButton, isfjButton, isfpButton,
            intjButton, infjButton, intpButton, infpButton,
            estjButton, esfjButton, estpButton, esfpButton,
            entjButton, enfjButton, entpButton, enfpButton;

    private int white, darkGray, red;  //button 색상지정
    private ArrayList<String> selectedFilters = new ArrayList<String>();
    private String currentSearchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Filtering> options =
                new FirebaseRecyclerOptions.Builder<Filtering>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), Filtering.class)
                        .build();

        myAdapter = new MyAdapter(options);
        recyclerView.setAdapter(myAdapter);


        initWidgets();
        setupData();
        setUpList();
        initColors();
        lookSelected(allButton);
        selectedFilters.add("all");

//        setContentView(R.layout.main_item);
//        Button chat_button = findViewById(R.id.chat_button);
//        chat_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, Chat.class);
//                startActivity(intent);
//
//            }
//        });

    }



    //Button 선언
    private void initWidgets()
    {
        allButton  = (Button) findViewById(R.id.allFilter);

        istjButton = (Button) findViewById(R.id.istjFilter);
        istpButton = (Button) findViewById(R.id.istpFilter);
        isfjButton = (Button) findViewById(R.id.isfjFilter);
        isfpButton = (Button) findViewById(R.id.isfpFilter);

        intjButton  = (Button) findViewById(R.id.intjFilter);
        infjButton = (Button) findViewById(R.id.infjFilter);
        intpButton = (Button) findViewById(R.id.intpFilter);
        infpButton = (Button) findViewById(R.id.infpFilter);

        estjButton = (Button) findViewById(R.id.estjFilter);
        esfjButton = (Button) findViewById(R.id.esfjFilter);
        estpButton = (Button) findViewById(R.id.estpFilter);
        esfpButton = (Button) findViewById(R.id.esfpFilter);

        entjButton = (Button) findViewById(R.id.entjFilter);
        enfjButton = (Button) findViewById(R.id.enfjFilter);
        entpButton = (Button) findViewById(R.id.entpFilter);
        enfpButton = (Button) findViewById(R.id.enfpFilter);
    }

    //color 선언
    private void initColors(){
        white = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        red = ContextCompat.getColor(getApplicationContext(), R.color.red);
        darkGray = ContextCompat.getColor(getApplicationContext(), R.color.darkerGray);
    }

    private void unSelectAllFilterButtons()
    {
        lookUnSelected(allButton);

        lookUnSelected(istjButton);
        lookUnSelected(istpButton);
        lookUnSelected(isfjButton);
        lookUnSelected(isfpButton);

        lookUnSelected(intjButton);
        lookUnSelected(infjButton);
        lookUnSelected(intpButton);
        lookUnSelected(infpButton);

        lookUnSelected(estjButton);
        lookUnSelected(esfjButton);
        lookUnSelected(estpButton);
        lookUnSelected(esfpButton);

        lookUnSelected(entjButton);
        lookUnSelected(enfjButton);
        lookUnSelected(entpButton);
        lookUnSelected(enfpButton);
    }

    //선택한 button 색상지정
    private void lookSelected(Button parsedButton)
    {
        parsedButton.setTextColor(white);
        parsedButton.setBackgroundColor(red);
    }

    //선택 안한 button 색상지정
    private void lookUnSelected(Button parsedButton)
    {
        parsedButton.setTextColor(red);
        parsedButton.setBackgroundColor(darkGray);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                mysearch(newText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchText = newText;
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

    //추가
    private void setupData()
    {
        Filtering istj = new Filtering("0","ISTJ", R.drawable.ic_account_circle);
        filterList.add(istj);
        Filtering istp = new Filtering("1","ISTP", R.drawable.ic_account_circle);
        filterList.add(istp);
        Filtering isfj = new Filtering("2","ISFJ", R.drawable.ic_account_circle);
        filterList.add(isfj);
        Filtering isfp = new Filtering("3","ISFP", R.drawable.ic_account_circle);
        filterList.add(isfp);

        Filtering intj = new Filtering("4","INTJ", R.drawable.ic_account_circle);
        filterList.add(intj);
        Filtering infj = new Filtering("5","INFJ", R.drawable.ic_account_circle);
        filterList.add(infj);
        Filtering intp = new Filtering("6", "INTP", R.drawable.ic_account_circle);
        filterList.add(intp);
        Filtering infp = new Filtering("7", "INFP", R.drawable.ic_account_circle);
        filterList.add(infp);

        Filtering estj = new Filtering("8", "ESTJ", R.drawable.ic_account_circle);
        filterList.add(estj);
        Filtering esfj = new Filtering("9", "ESFJ", R.drawable.ic_account_circle);
        filterList.add(esfj);
        Filtering estp = new Filtering("10", "ESTP", R.drawable.ic_account_circle);
        filterList.add(estp);
        Filtering esfp = new Filtering("11", "ESFP", R.drawable.ic_account_circle);
        filterList.add(esfp);

        Filtering entj = new Filtering("11", "ENTJ", R.drawable.ic_account_circle);
        filterList.add(entj);
        Filtering enfj = new Filtering("11", "ENFJ", R.drawable.ic_account_circle);
        filterList.add(enfj);
        Filtering entp = new Filtering("11", "ENTP", R.drawable.ic_account_circle);
        filterList.add(entp);
        Filtering enfp = new Filtering("11", "ENFP", R.drawable.ic_account_circle);
        filterList.add(enfp);
    }

    //추가
    private void setUpList()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recview);

        setAdapter(filterList);
    }

    //filterList status (상태)
    private void filteringList(String status)
    {
        if(status != null && !selectedFilters.contains(status))
            selectedFilters.add(status);

        ArrayList<Filtering> filteredShapes = new ArrayList<Filtering>();

        for(Filtering filtering: filterList)
        {
            for(String filter: selectedFilters)
            {
                if(filtering.getMbti().toLowerCase().contains(filter))
                {
                    if(currentSearchText == "")
                    {
                        filteredShapes.add(filtering);
                    }
                    else
                    {
                        if(filtering.getMbti().toLowerCase().contains(currentSearchText.toLowerCase()))
                        {
                            filteredShapes.add(filtering);
                        }
                    }
                }
            }
        }
        setAdapter(filteredShapes);
    }

    //button click
    public void allFilterTapped(View view)
    {
        selectedFilters.clear();
        selectedFilters.add("all");

        unSelectAllFilterButtons();
        lookSelected(allButton);

        setAdapter(filterList);
    }

    public void istjFilterTapped(View view)
    {
        filteringList("istj");
        lookSelected(istjButton);
        lookUnSelected(allButton);
    }

    public void istpFilterTapped(View view)
    {
        filteringList("istp");
        lookSelected(istpButton);
        lookUnSelected(allButton);
    }

    public void isfjFilterTapped(View view)
    {
        filteringList("isfj");
        lookSelected(isfjButton);
        lookUnSelected(allButton);
    }

    public void isfpFilterTapped(View view)
    {
        filteringList("isfp");
        lookSelected(isfpButton);
        lookUnSelected(allButton);
    }

    public void intjFilterTapped(View view)
    {
        filteringList("intj");
        lookSelected(intjButton);
        lookUnSelected(allButton);
    }

    public void infjFilterTapped(View view)
    {
        filteringList("infj");
        lookSelected(infjButton);
        lookUnSelected(allButton);
    }

    public void intpFilterTapped(View view)
    {
        filteringList("intp");
        lookSelected(intpButton);
        lookUnSelected(allButton);
    }

    public void infpFilterTapped(View view)
    {
        filteringList("infp");
        lookSelected(infpButton);
        lookUnSelected(allButton);
    }

    public void estjFilterTapped(View view)
    {
        filteringList("estj");
        lookSelected(estjButton);
        lookUnSelected(allButton);
    }
    public void esfjFilterTapped(View view)
    {
        filteringList("esfj");
        lookSelected(esfjButton);
        lookUnSelected(allButton);
    }
    public void estpFilterTapped(View view)
    {
        filteringList("estp");
        lookSelected(estpButton);
        lookUnSelected(allButton);
    }
    public void esfpFilterTapped(View view)
    {
        filteringList("esfp");
        lookSelected(esfpButton);
        lookUnSelected(allButton);
    }

    public void entjFilterTapped(View view)
    {
        filteringList("entj");
        lookSelected(entjButton);
        lookUnSelected(allButton);
    }
    public void enfjFilterTapped(View view)
    {
        filteringList("enfj");
        lookSelected(enfjButton);
        lookUnSelected(allButton);
    }
    public void entpFilterTapped(View view)
    {
        filteringList("entp");
        lookSelected(entpButton);
        lookUnSelected(allButton);
    }
    public void enfpFilterTapped(View view)
    {
        filteringList("enfp");
        lookSelected(enfpButton);
        lookUnSelected(allButton);
    }

    private void setAdapter(ArrayList<Filtering> filterList)
    {
        FirebaseRecyclerOptions<Filtering> options =
                new FirebaseRecyclerOptions.Builder<Filtering>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("mbti"), Filtering.class)
                        .build();

        myAdapter = new MyAdapter(options);
        myAdapter.startListening();
        recyclerView.setAdapter(myAdapter);
    }
}