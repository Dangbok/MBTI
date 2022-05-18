package com.example.login5;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    ImageView iv_image;
    EditText et_email, et_pass, et_name, et_age, et_mbti, et_myself;
    Button btn_register1;
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private final DatabaseReference root=FirebaseDatabase.getInstance().getReference("Users"); //실시간 데이터베이스 연동 객체
    private final StorageReference reference=FirebaseStorage.getInstance().getReference("Profile"); //스토리지에 접근하기 위해 사용
    private Uri imageUri;


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


        iv_image = findViewById(R.id.iv_image);
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_mbti = findViewById(R.id.et_mbti);
        et_myself = findViewById(R.id.et_myself);
        btn_register1 = findViewById(R.id.btn_register1);


        //갤러리접근 리스너
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/");
                activityResult.launch(galleryIntent);
                }
        });


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

                //프로필사진,이름,이메일,비밀번호 중 하나라도 비었으면 return
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(name) || TextUtils.isEmpty(age) || TextUtils.isEmpty(mbti) ||
                        TextUtils.isEmpty(myself) || imageUri == null) {
                    Toast.makeText(RegisterActivity.this, "정보를 바르게 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                //파이어베이스에 신규계정 등록하기
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //가입 성공시
                        if (task.isSuccessful()) {

                            //각 사용자에 Uid 할당
                            final String uid = task.getResult().getUser().getUid();

                            //스토리지에 사진파일 저장 경로
                            StorageReference fileRef=reference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

                            //스토리지에 사진파일 저장함과 동시에
                            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                                    while (!imageUrl.isComplete()) ;

                                    // database에 User클래스에 맞게 데이터 저장
                                    User user = new User();

                                    user.uid = uid;
                                    user.email = email;
                                    user.pass = pass;
                                    user.name=name;
                                    user.age = Integer.parseInt(age);
                                    user.mbti = mbti;
                                    user.myself = myself;
                                    user.profile = imageUrl.getResult().toString();

                                    root.child(uid)
                                            .setValue(user);
                                }

                            });


                            //가입이 이루어져을시 가입 화면을 빠져나감.
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                            return;  //해당 메소드 진행을 멈추고 빠져나감.
                        }
                    }
                });
            }
        });
    }


    //갤러리접근 리스너의 activityResult 인자 받아서 수행
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                // Uri
                imageUri= result.getData().getData();

                //Glide로 이미지 불러오는 모양 변화(아이폰은 heic파일형식으로 저장되어 추후 변화해햐 함)
                RequestOptions cropOptions=new RequestOptions();
                Glide.with(getApplicationContext()).load(imageUri).apply(cropOptions.optionalCircleCrop()).into(iv_image);

            }
        }
    });

    //파일타입 가져오기
    private String getFileExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }
}