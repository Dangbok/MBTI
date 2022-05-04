package com.example.login5;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    //회원가입 입력필드(이메일,비밀번호,이름,나이,mbti,자기소개)
    private EditText et_id, et_password, et_name, et_mbti;
    private ImageView iv_iv;

    // 버튼필드(사진 등록, 회원가입, 뒤로가기)
    private Button btn_im, btn_register, btn_back;

    // 파이어베이스 처리
    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증처리
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage storage;
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스 연동 객체

    private DBUser dbUser;

    // 액티비티 시작시 처음으로 실행되는 생명주기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_id = findViewById(R.id.et_id); //아이디
        et_password = findViewById(R.id.et_password); //비밀번호
        et_name = findViewById(R.id.et_name); //이름
        et_mbti = findViewById(R.id.et_mbti); //mbti
        btn_im = findViewById(R.id.btn_im); //사진 등록하기 버튼
        btn_register = findViewById(R.id.btn_register); //회원가입 버튼
        btn_back = findViewById(R.id.btn_back); //뒤로가기 버튼

        //파이어베이스 이용
        mFirebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("login");

        // 모든 정보 입력 후 회원가입 버튼 클릭 시 수행
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // EditText에 현재 입력되어 있는 값을 get(가져온다)해온다.
                String userID = et_id.getText().toString().trim();
                String userPass = et_password.getText().toString().trim();
                String userName = et_name.getText().toString().trim();
                String userMbti = et_mbti.getText().toString().trim();
//                String userMyself = et_myself.getText().toString().trim();

                UserAccount userAccount = new UserAccount(String userID, String userPass, String userName, String userMbti);

                //Firebase Auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(userID, userPass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount("", userID, userPass, userName, userMbti);
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(userPass);
                            account.setName(firebaseUser.get);
                            account.setMbti(firebaseUser.);

                            // setValue : 데이터베이스에 insert(삽입) 행위
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            public void onDataChage (@NonNull DataSnapshot dataSnapshot){
                                UserAccount group = dataSnapshot.getValue(UserAccount.class);

                                name = group.getName();
                                age = group.getAge();
                                userMbti[0] = group.getName();
                                userMyself[0] = group.getName();


                            }
                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }

                        // 사진 등록하기 버튼 클릭시 이동
                        btn_im.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(RegisterActivity.this, ImageActivity.class);
                                startActivity(intent);  //액티비티 이동
                            }
                        });

                        // 뒤로가기 버튼 클릭시 이동
                        btn_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);  //액티비티 이동
                            }
                        });
                    }
                });
            }
        });
    }
}