//package com.example.login5;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.loader.content.CursorLoader;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.io.File;
//
//public class RegisterActivity1 extends AppCompatActivity {
//
//    private static final String TAG = "RegisterActivity";
//    ImageView iv_iv;
//    EditText et_email, et_pass, et_name, et_age, et_mbti, et_myself;
//    Button btn_register1;
//
//    private FirebaseAuth firebaseAuth;
//    private FirebaseDatabase firebaseDatabase;
//    private DatabaseReference databaseReference; //실시간 데이터베이스 연동 객체
//    private FirebaseStorage firebaseStorage; //파이어스토어에 접근하기 위해 사용
//    private StorageReference storageReference;
//
//    private Uri imageUri; //갤러리 사진 가져오기 위한 변수
//    private String pathUri;
//    private File tempFile;
//    public static final int PICK_FROM_ALBUM = 1;
//
////    private String[] permissionList = {Manifest.permission.READ_EXTERNAL_STORAGE};
////
////    //권한 체크 함수
////    public void checkPermission() {
////        //현재 버전 6.0 미만이면 종료 --> 6이후 부터 권한 허락
////        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
////
////        //각 권한 허용 여부를 확인
////        for (String permission : permissionList) {
////            int chk = checkCallingOrSelfPermission(permission);
////            //거부 상태라면
////            if (chk == PackageManager.PERMISSION_DENIED) {
////                //사용자에게 권한 허용여부를 확인하는 창을 띄운다.
////                requestPermissions(permissionList, 0); //권한 검사 필요한 것들만 남는다.
////                break;
////            }
////        }
////    }
//
//
//    // 액티비티 시작시 처음으로 실행되는 생명주기
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        //액션 바 등록하기
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("회원가입");
//
//        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기버튼
//        actionBar.setDisplayShowHomeEnabled(true); //홈 아이콘
//
//        //파이어베이스 접근 설정
//        // user = firebaseAuth.getCurrentUser();
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();
////        databaseReference = FirebaseDatabase.getInstance().getReference("Users"); //실시간 데이터베이스
//        firebaseStorage = FirebaseStorage.getInstance(); //스토리지에 접근하기 위한 인스턴스 선언
//        storageReference = FirebaseStorage.getInstance().getReference();
//
//        iv_iv = findViewById(R.id.iv_iv);
//        et_email = findViewById(R.id.et_email);
//        et_pass = findViewById(R.id.et_pass);
//        et_name = findViewById(R.id.et_name);
//        et_age = findViewById(R.id.et_age);
//        et_mbti = findViewById(R.id.et_mbti);
//        et_myself = findViewById(R.id.et_myself);
//        btn_register1 = findViewById(R.id.btn_register1);
//
//        // 사진클릭시 앨범 이동
//        iv_iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gotoAlbum();
//            }
//        });
//
//        //가입버튼 클릭리스너   -->  firebase에 데이터를 저장한다.
//        btn_register1.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                register();
//            }
//        });
//    }
//
//    // 앨범 메소드
//    private void gotoAlbum() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//        startActivityForResult(intent, PICK_FROM_ALBUM);
//    }
//
//
//    protected void onActiviy(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode != RESULT_OK) { // 코드가 틀릴경우
//            Toast.makeText(RegisterActivity.this, "취소 되었습니다", Toast.LENGTH_SHORT).show();
//            if (tempFile != null) {
//                if (tempFile.exists()) {
//                    if (tempFile.delete()) {
//                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
//                        tempFile = null;
//                    }
//                }
//            }
//            return;
//        }
//
//        switch (requestCode) {
//            case PICK_FROM_ALBUM: { // 코드 일치
//                // Uri
//                imageUri = data.getData();
//                pathUri = getPath(data.getData());
//                Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + imageUri);
//                iv_iv.setImageURI(imageUri); // 이미지 띄움
//                break;
//            }
//        }
//    }
//
//    // uri 절대경로 가져오기
//    public String getPath(Uri uri) {
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
//
//
//    // 회원가입 메소드
//    private void register() {
//
//        //가입 정보 가져오기
//        final String email = et_email.getText().toString().trim();
//        String pass = et_pass.getText().toString().trim();
//        String name = et_name.getText().toString().trim();
//        String age = et_age.getText().toString().trim();
//        String mbti = et_mbti.getText().toString().trim();
//        String myself = et_myself.getText().toString().trim();
//
//        // 프로필사진,이름,이메일,비밀번호 중 하나라도 비었으면 return
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(name) || TextUtils.isEmpty(age) ||
//                TextUtils.isEmpty(mbti) || TextUtils.isEmpty(myself) || iv_iv == null) {
//            Toast.makeText(RegisterActivity.this, "정보를 바르게 입력해 주세요", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            //파이어베이스에 신규계정 등록하기
//            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                    //가입 성공시
//                    if (task.isSuccessful()) {
//                        final String uid = task.getResult().getUser().getUid();
//                        final Uri file = Uri.fromFile(new File(pathUri)); // path
//
////                        FirebaseUser user = firebaseAuth.getCurrentUser();
////                        final String email = user.getEmail();
////                        final String uid = user.getUid();
////                        final String pass = et_pass.getText().toString().trim();
////                        final String name = et_name.getText().toString().trim();
////                        final String age = et_age.getText().toString().trim();
////                        final String mbti = et_mbti.getText().toString().trim();
////                        final String myself = et_myself.getText().toString().trim();
//
//                        // 스토리지에 방생성 후 선택한 이미지 넣음
//                        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
//                                .child("usersprofileImages").child("uid/" + file.getLastPathSegment());
//                        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                                final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
//                                while (!imageUrl.isComplete()) ;
//
//                                User user = new User();
//
//                                user.uid = uid;
//                                user.email = email;
//                                user.password = pass;
//                                user.name = name;
//                                user.age = Integer.parseInt(age);
//                                user.mbti = mbti;
//                                user.myself = myself;
//                                user.profile = imageUrl.getResult().toString();
//
//                                // database에 저장
//                                FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
//                                        .setValue(user);
//                            }
//                        });
//
//                        //가입이 이루어져을시 가입 화면을 빠져나감.
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                        Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
//                        return;  //해당 메소드 진행을 멈추고 빠져나감.
//
//                    }
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        ; // 뒤로가기 버튼이 눌렸을시
//        return super.onSupportNavigateUp(); // 뒤로가기 버튼
//    }
//}

//import androidx.annotation.NonNull;FirebaseStorage storage = FirebaseStorage.getInstance("gs://mbti-matching-users.appspot.com");
//        StorageReference storageRef = storage.getReference();
//        storageRef.child("photo/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//@Override
//public void onSuccess(Uri uri) {
//        //이미지 로드 성공시
//
//        Glide.with(getApplicationContext())
//        .load(uri)
//        .into(iv_image);
//
//        }
//        }).addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull Exception exception) {
//        //이미지 로드 실패시
//        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
//        }
//        });
//        }