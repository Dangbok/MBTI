package com.example.login5;

import android.content.Intent;
import android.database.Cursor;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class RegisterActivity extends AppCompatActivity {

    //회원가입 입력필드(이메일,비밀번호,이름,나이,mbti,자기소개)
    private EditText et_id, et_password, et_name, et_age, et_mbti, et_myself;
    private ImageView iv_iv;

    // 버튼필드(사진 등록, 회원가입, 뒤로가기)
    private Button btn_im, btn_register, btn_back;

    // 파이어베이스 처리
    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증처리
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage storage;
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스 연동 객체

    // 사진 업로드
    private String imageUri = "";
    private int GALLERY_CODE = 10;

    private DBUser dbUser;

//    private File profileIconFile;
//    private Uri mTempImageUri;

    // 액티비티 시작시 처음으로 실행되는 생명주기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        iv_iv = findViewById(R.id.iv_iv);
        et_id = findViewById(R.id.et_id); //아이디
        et_password = findViewById(R.id.et_password); //비밀번호
        et_name = findViewById(R.id.et_name); //이름
        et_age = findViewById(R.id.et_age); //나이
        et_mbti = findViewById(R.id.et_mbti); //mbti
        et_myself = findViewById(R.id.et_myself); //자기소개
        btn_im = findViewById(R.id.btn_im); //사진 등록하기 버튼
        btn_register = findViewById(R.id.btn_register); //회원가입 버튼
        btn_back = findViewById(R.id.btn_back); //뒤로가기 버튼

        //파이어베이스 이용
        mFirebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("login");


        iv_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });

        // 모든 정보 입력 후 회원가입 버튼 클릭 시 수행
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // EditText에 현재 입력되어 있는 값을 get(가져온다)해온다.
                String userID = et_id.getText().toString().trim();
                String userPass = et_password.getText().toString().trim();
                String userName = et_name.getText().toString().trim();
                int userAge = Integer.parseInt(et_age.getText().toString().trim());
                String userMbti = et_mbti.getText().toString().trim();
                String userMyself = et_myself.getText().toString().trim();

                uploadImg(imageUri);

                UserAccount userAccount = new UserAccount("", userID, userPass, userName, userAge, userMbti, userMbti, userMyself);

                dbUser.add(userAccount).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();

                        et_id.setText("");
                        et_password.setText("");
                        et_name.setText("");
                        et_age.setText("");
                        et_mbti.setText("");
                        et_myself.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "실패:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //Firebase Auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(userID, userPass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount("", userID, userPass, userName, userAge, userMbti, userMyself, imageUri);
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(userPass);
                            account.setName(firebaseUser.getDisplayName());
                            account.setAge(firebaseUser.getMetadata().describeContents());
                            account.setMbti(firebaseUser.getMetadata());
                            account.setMbti(firebaseUser.getMetadata());
                            account.setImageUrl(imageUri);

                            // setValue : 데이터베이스에 insert(삽입) 행위
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
//                    }
//                });

//            }
//        });

                        // 사진 등록하기 버튼 클릭시 이동
//        btn_im.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RegisterActivity.this, ImageActivity.class);
//                startActivity(intent);  //액티비티 이동
//            }
//        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_CODE) {
            imageUri = getRealPathFromUri(data.getData());
            RequestOptions cropOptions = new RequestOptions();
            Glide.with(getApplicationContext())
                    .load(imageUri)
                    .apply(cropOptions.optionalCircleCrop())
                    .into(iv_iv);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //절대경로를 구한다.
    private String getRealPathFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String url = cursor.getString(columnIndex);
        cursor.close();
        return url;
    }

    private void uploadImg(String uri) {
        try {
            StorageReference storageRef = storage.getReference();

            Uri file = Uri.fromFile(new File(uri));
            final StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return riversRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();

                        //파이어베이스에 데이터베이스 업로드
                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUrl = task.getResult();

                        mDatabaseRef.child("UserAccount").child("Profile").push().setValue(imageUri);

                    } else {
                    }
                }
            });
        } catch (NullPointerException e) {
            Toast.makeText(RegisterActivity.this, "이미지 선택 안함", Toast.LENGTH_SHORT).show();
        }
    }
}
    
    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case 1:
//                if (resultCode == RESULT_OK) {
//                    Uri uri = data.getData();
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    iv_image.setImageBitmap(bitmap);
//                    iv_image.setImageURI(uri);
//                }
//                break;
//        }
//    }