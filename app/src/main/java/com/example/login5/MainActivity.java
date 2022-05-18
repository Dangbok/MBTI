package com.example.login5;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        recyclerView=findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);
//        layoutManager=new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        arrayList=new ArrayList<>();
//
//        database=FirebaseDatabase.getInstance();
//        databaseReference=database.getReference("Users");
//
//        databaseReference.child("uid").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                arrayList.clear();
//
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    User user=dataSnapshot.getValue(User.class);
//                    arrayList.add(user);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("Mainactiviy", String.valueOf(database),error.toException());
//
//            }
//        });
//
//        adapter=new CustomAdapter(arrayList,this);
//        recyclerView.setAdapter(adapter);
    }
}