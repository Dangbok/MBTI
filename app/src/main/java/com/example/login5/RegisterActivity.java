package com.example.login5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    EditText et_email, et_pass, et_name,et_age, et_mbti,et_myself;
    Button btn_register1;
    private FirebaseAuth firebaseAuth;


    // 액티비티 시작시 처음으로 실행되는 생명주기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //액션 바 등록하기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("회원가입");

        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기버튼
        actionBar.setDisplayShowHomeEnabled(true); //홈 아이콘

        //파이어베이스 접근 설정
        // user = firebaseAuth.getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_mbti = findViewById(R.id.et_mbti);
        et_myself = findViewById(R.id.et_myself);
        btn_register1=findViewById(R.id.btn_register1);


        //가입버튼 클릭리스너   -->  firebase에 데이터를 저장한다.
        btn_register1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //가입 정보 가져오기
                final String email = et_email.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                String name = et_name.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String mbti = et_mbti.getText().toString().trim();
                String myself = et_myself.getText().toString().trim();


                //파이어베이스에 신규계정 등록하기
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //가입 성공시
                        if (task.isSuccessful()) {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String pass = et_pass.getText().toString().trim();
                            String name = et_name.getText().toString().trim();
                            String age = et_age.getText().toString().trim();
                            String mbti = et_mbti.getText().toString().trim();
                            String myself = et_myself.getText().toString().trim();

                            //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                            HashMap<Object, String> hashMap = new HashMap<>();

                            hashMap.put("uid", uid);
                            hashMap.put("email", email);
                            hashMap.put("password", pass);
                            hashMap.put("name", name);
                            hashMap.put("age", age);
                            hashMap.put("MBTI", mbti);
                            hashMap.put("myself", myself);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);


                            //가입이 이루어져을시 가입 화면을 빠져나감.
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                            return;  //해당 메소드 진행을 멈추고 빠져나감.

                        }
                    }
                });
            }
        });
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }
}


//        et_id = findViewById(R.id.et_id); //아이디
//        et_password = findViewById(R.id.et_password); //비밀번호
//        et_name = findViewById(R.id.et_name); //이름
//        et_mbti = findViewById(R.id.et_mbti); //mbti

//        btn_im = findViewById(R.id.btn_im); //사진 등록하기 버튼
//        btn_register = findViewById(R.id.btn_register); //회원가입 버튼
//        btn_back = findViewById(R.id.btn_back); //뒤로가기 버튼
//
//        //파이어베이스 이용
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        storage = FirebaseStorage.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        mDatabaseRef = FirebaseDatabase.getInstance().getReference("login");
//
//        // 모든 정보 입력 후 회원가입 버튼 클릭 시 수행
//        btn_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // EditText에 현재 입력되어 있는 값을 get(가져온다)해온다.
//                String userID = et_id.getText().toString().trim();
//                String userPass = et_password.getText().toString().trim();
//                String userName = et_name.getText().toString().trim();
//                String userMbti = et_mbti.getText().toString().trim();
////                String userMyself = et_myself.getText().toString().trim();
//
//                UserAccount userAccount = new UserAccount(String userID, String userPass, String userName, String userMbti);
//
//                //Firebase Auth 진행
//                mFirebaseAuth.createUserWithEmailAndPassword(userID, userPass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
//                            UserAccount account = new UserAccount("", userID, userPass, userName, userMbti);
//                            account.setIdToken(firebaseUser.getUid());
//                            account.setEmailId(firebaseUser.getEmail());
//                            account.setPassword(userPass);
//                            account.setName(firebaseUser.get);
//                            account.setMbti(firebaseUser.);
//
//                            // setValue : 데이터베이스에 insert(삽입) 행위
//                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
//
//                            public void onDataChage (@NonNull DataSnapshot dataSnapshot){
//                                UserAccount group = dataSnapshot.getValue(UserAccount.class);
//
//                                name = group.getName();
//                                age = group.getAge();
//                                userMbti[0] = group.getName();
//                                userMyself[0] = group.getName();
//
//
//                            }
//                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
//                        }
//
//                        // 사진 등록하기 버튼 클릭시 이동
//                        btn_im.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(RegisterActivity.this, ImageActivity.class);
//                                startActivity(intent);  //액티비티 이동
//                            }
//                        });
//
//                        // 뒤로가기 버튼 클릭시 이동
//                        btn_back.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                startActivity(intent);  //액티비티 이동
//                            }
//                        });
//                    }
//                });
//            }
//        });
//    }