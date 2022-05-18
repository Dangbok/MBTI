package com.example.login5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth; //파이어베이스 인증처리
    private DatabaseReference databaseReference; //실시간 데이터베이스 연동 객체
    private StorageReference storageReference;
    private EditText et_id,et_password; //로그인 입력필드


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        et_id=findViewById(R.id.et_id);
        et_password=findViewById(R.id.et_password);

        Button btn_mbti = findViewById(R.id.btn_mbti);
        Button btn_login = findViewById(R.id.btn_login);
        Button btn_register = findViewById(R.id.btn_register);

        //MBTI검사 웹페이지 이동 버튼 실행
        btn_mbti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MbtiActivity.class);
                startActivity(intent);  //액티비티 이동
            }
        });

        //로그인 요청
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String형 변수 이메일,비밀번호(EditText에서 받아오는 값)으로 로그인하는 것
                String userID=et_id.getText().toString().trim();
                String userPass=et_password.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(userID,userPass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //로그인 성공!!!
                                    Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this,"로그인에 성공하셨습니다.",Toast.LENGTH_SHORT).show();
//                                    finish(); //현재 액티비티 파괴
                                }else{
                                    Toast.makeText(LoginActivity.this,"로그인에 실패하셨습니다.",Toast.LENGTH_SHORT).show();

                                }

                            }
                        }
                );
            }
        });

        //회원가입버튼 클릭시 화면 전환
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);  //액티비티 이동
            }
        });
    }
}
