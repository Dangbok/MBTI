package com.example.login5;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    ImageView iv_image;
    EditText et_email, et_pass, et_name, et_age, et_mbti, et_myself;
    Button btn_register1,btn_iv;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference; //실시간 데이터베이스 연동 객체
    private FirebaseStorage storage; //파이어스토어에 접근하기 위해 사용

    private String imageUri; //갤러리 사진 가져오기 위한 변수
    private String pathUri;
    private File tempFile;
//    public static final int PICK_FROM_ALBUM = 1;

    private String[] permissionList = {Manifest.permission.READ_EXTERNAL_STORAGE};

    //권한 체크 함수
    public void checkPermission() {
        //현재 버전 6.0 미만이면 종료 --> 6이후 부터 권한 허락
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;

        //각 권한 허용 여부를 확인
        for (String permission : permissionList) {
            int chk = checkCallingOrSelfPermission(permission);
            //거부 상태라면
            if (chk == PackageManager.PERMISSION_DENIED) {
                //사용자에게 권한 허용여부를 확인하는 창을 띄운다.
                requestPermissions(permissionList, 0); //권한 검사 필요한 것들만 남는다.
                break;
            }
        }
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // Uri
                imageUri = String.valueOf(result.getData());
                pathUri = String.valueOf(result.getData());
                Glide.with(RegisterActivity.this).load(imageUri).into(iv_image);
                Log.d(TAG, "photoUri : " + imageUri);
                iv_image.setImageURI(Uri.parse(imageUri)); // 이미지 띄움
            }

        }
    });


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
        reference = FirebaseDatabase.getInstance().getReference("Users"); //실시간 데이터베이스
        storage = FirebaseStorage.getInstance(); //스토리지에 접근하기 위한 인스턴스 선언
        //firebaseDatabase = FirebaseDatabase.getInstance().getReference();


        iv_image = findViewById(R.id.iv_image);
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_mbti = findViewById(R.id.et_mbti);
        et_myself = findViewById(R.id.et_myself);
        btn_iv=findViewById(R.id.btn_iv);
        btn_register1 = findViewById(R.id.btn_register1);


        //갤러리접근 리스너
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAlbum();
            }
        });

        //사진등록 리스너
        btn_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
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


                //파이어베이스에 신규계정 등록하기
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //가입 성공시
                        if (task.isSuccessful()) {
                            final Uri file = Uri.fromFile(new File(pathUri)); // path

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String pass = et_pass.getText().toString().trim();
                            String name = et_name.getText().toString().trim();
                            String age = et_age.getText().toString().trim();
                            String mbti = et_mbti.getText().toString().trim();
                            String myself = et_myself.getText().toString().trim();

                            // 스토리지에 방생성 후 선택한 이미지 넣음
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                                    .child("profile").child("uid/" + file.getLastPathSegment());

                            storageReference.putFile(Uri.parse(imageUri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url = uri.toString();
                                        }
                                    });
                                }
                            });



                            //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                            HashMap<Object, String> hashMap = new HashMap<>();

                            hashMap.put("uid", uid);
                            hashMap.put("email", email);
                            hashMap.put("password", pass);
                            hashMap.put("name", name);
                            hashMap.put("age", age);
                            hashMap.put("MBTI", mbti);
                            hashMap.put("myself", myself);
                            hashMap.put("profile", String.valueOf(imageUri));

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);


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

    private void gotoAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startForResult.launch(intent);
        setResult(RESULT_OK,intent);
    }

    private void upload() {
        if(iv_image!=null){

            SimpleDateFormat sdf=new SimpleDateFormat("yyyymmdd_hhmmss");
            String filename=sdf.format(new Date())+".png";

            FirebaseStorage storage=FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference riversRef = storageRef.child("photo/");
            UploadTask uploadTask = riversRef.putFile(Uri.parse(imageUri));

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(RegisterActivity.this, "사진이 정상적으로 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });

        } else{
            Toast.makeText(RegisterActivity.this, "사진이 정상적으로 업로드 되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }


//    @Override
//    protected void OnActivityResult( int requestCode, int resultCode, @Nullable Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//
//        switch (requestCode) {
//            case PICK_FROM_ALBUM: { // 코드 일치
//                // Uri
//                imageUri = data.getData();
//                pathUri = getPath(data.getData());
//                Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + imageUri);
//                iv_image.setImageURI(imageUri); // 이미지 띄움
//                break;
//            }
//        }
//    }

//    // uri 절대경로 가져오기
//    public String getPath(Intent uri) {
//
//        String[] proj = {MediaStore.Images.Media.DATA};
//        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
//
//        Cursor cursor = cursorLoader.loadInBackground();
//        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//        cursor.moveToFirst();
//        return cursor.getString(index);
//    }

    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }
}