//package com.example.login5;
//
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class DBUser {
//
//    private DatabaseReference databaseReference;
//    private FirebaseDatabase firebaseDatabase;
//    private Object UserAccount;
//
//    DBUser(){
//        FirebaseDatabase db=FirebaseDatabase.getInstance();
//        firebaseDatabase=FirebaseDatabase.getInstance();
//        databaseReference=db.getReference(UserAccount.class.getSimpleName());
//    }
//
//    //등록
//    public Task<Void> add(UserAccount userAccount){
//        return databaseReference.push().setValue(UserAccount);
//    }
//
//}
