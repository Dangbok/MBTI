package com.example.login5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class ImageActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증처리
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스 연동 객체
    private FirebaseStorage storage; //파이어스토어에 접근하기 위해 사용
    private Uri uri; //갤러리 사진 가져오기 위한 변수
    private int GALLERY_CODE=10;

    ImageView iv_im; //사진 필드
    Button btn_ima; //사진 불러오기 버튼
    private Object Tag;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mFirebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 이용
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("login"); //실시간 데이터베이스
        storage = FirebaseStorage.getInstance(); //스토리지에 접근하기 위한 인스턴스 선언

        iv_im = findViewById(R.id.iv_im); //사진
        btn_ima = findViewById(R.id.btn_ima); //사진 등록하기 버튼

        // 이미지 클릭시 바로 갤러리 접근
//        iv_im.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (view.getId()) {
//                    case R.id.iv_im:
//                        loadAlbum();
//                        break;
//                }
//            }
//        });

        // 사진 등록하기 버튼 클릭시 수행
        btn_ima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 1);
            }
        });
    }
//                switch (view.getId()){
//                    case R.id.btn_ima:
//                        clickUpload();
//                }
//            }
//        });
//    }

//    private void clickUpload() {
//        if(uri!=null){
//            final ProgressDialog progressDialog=new ProgressDialog(this);
//            progressDialog.setTitle("업로드중...");
//            progressDialog.show();

//            SimpleDateFormat sdf=new SimpleDateFormat("yyyymmdd_hhmmss");
//            String filename=sdf.format(new Date())+".png";

//            FirebaseStorage storage=FirebaseStorage.getInstance();
//            StorageReference storageRef = storage.getReference();
//            StorageReference riversRef = storageRef.child("photo/1.png");
//            UploadTask uploadTask = riversRef.putFile(uri);
//
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progressDialog.dismiss();
//                    Toast.makeText(ImageActivity.this, "사진이 정상적으로 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
////                    progressDialog.dismiss();
//                    Toast.makeText(ImageActivity.this, "사진이 정상적으로 업로드 되지 않았습니다.", Toast.LENGTH_SHORT).show();
//                }
//            });
//            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                private FileDownloadTask.TaskSnapshot taskSnapshot;
//
//                @Override
//                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                    @SuppressWarnings("VisibleForTests")
//                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
//                    //dialog에 진행률을 퍼센트로 출력해 준다
//                    progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
//                }
//            });
//        } else{
//            Toast.makeText(ImageActivity.this, "사진이 정상적으로 업로드 되지 않았습니다.", Toast.LENGTH_SHORT).show();
//        }
//    }

    // 바로 갤러리에 접근 가능하게 하기 위한 함수 선언
//    private void loadAlbum() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//        startActivityForResult(intent, 1);
//    }

    // 갤러리 사진을 파이어스토어와 연동시켜 사진 업로드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            try {
                StorageReference storageRef = storage.getReference();
                Uri file = Uri.fromFile(new File(String.valueOf(uri)));
                final StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                            Toast.makeText(ImageActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                            @SuppressWarnings("VisibleForTests")
                            Uri downloadUrl = task.getResult();
                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            startActivity(intent);
                        } else {

                        }
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private String getRealPathFromUri(Uri uri)
    {
        String[] proj=  {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columnIndex);
        cursor.close();
        return  result;
    }
}

//        switch (requestCode) {
//            case 1:
//                if (resultCode == RESULT_OK && data.getData() !=null) {
//                    Uri uri = data.getData();
//                    Glide.with(getApplicationContext()).load(uri).into(iv_im);
//                    Bitmap bitmap = null;
//                    try {
////                        InputStream in = getContentResolver().openInputStream(data.getData());
////                        Bitmap img = BitmapFactory.decodeStream(in);
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
////                        in.close();
//                        iv_im.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else{
//                    Toast.makeText(this, "이미지 선택을 하지 않았습니다.", Toast.LENGTH_SHORT).show();
//                    }
//                break;

//            case 2:
//                if (resultCode == RESULT_OK) {
//                    StorageReference storageRef = storage.getReference();
//                    StorageReference riversRef = storageRef.child("photo/"+fileList());
//                    UploadTask uploadTask = riversRef.putFile(uri);
//
//                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(ImageActivity.this, "사진이 정상적으로 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
////                    try {
////                        InputStream in = getContentResolver().openInputStream(data.getData());
////                        Bitmap img = BitmapFactory.decodeStream(in);
////                        in.close();
////                        iv_im.setImageBitmap(img);
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
//                }
//        }
//    }
//}

                //이미지 크롭
//                intent.setAction(intent.ACTION_GET_CONTENT);
//                intent.putExtra("npFaceDetection", true);
//                intent.putExtra("crop", "true");
//                intent.putExtra("scale", true);
//                intent.putExtra("aspectX", 16);
//                intent.putExtra("aspectY", 9);
//                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case 1:
//                if (resultCode == RESULT_OK) {
//                    Uri uri = data.getData();
//                    if(uri!=null){
//                        iv_im.setImageURI(uri);
//                        Glide.with(getApplicationContext()).load(uri).into(iv_im);
//                    }
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                        iv_im.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else{
//                    Toast.makeText(this,"이미지 선택을 하지 않았습니다.",Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }