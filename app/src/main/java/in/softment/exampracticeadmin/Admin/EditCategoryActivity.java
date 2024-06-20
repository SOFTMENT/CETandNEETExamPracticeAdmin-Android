package in.softment.exampracticeadmin.Admin;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;

import in.softment.exampracticeadmin.Model.CategoryModel;
import in.softment.exampracticeadmin.R;
import in.softment.exampracticeadmin.Util.ProgressHud;
import in.softment.exampracticeadmin.Util.Services;

public class EditCategoryActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 4655;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri resultUri = null;
    private ImageView imageView;
    private CategoryModel categoryModel = null;
    private boolean isImageSelected = false;
    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), new ActivityResultCallback<CropImageView.CropResult>() {
        @Override
        public void onActivityResult(CropImageView.CropResult result) {
            if (result.isSuccessful()) {
                Uri uri = result.getUriContent();
                Bitmap bitmap = null;
                try {
                    isImageSelected =  true;
                    resultUri = uri;
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);


                } catch (IOException e) {

                }
            }
            else {
                Services.showDialog(EditCategoryActivity.this, "ERROR",result.getError().getLocalizedMessage());
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);


        categoryModel = (CategoryModel) getIntent().getSerializableExtra("category");
        if (categoryModel == null) {
            finish();
        }

        //BACK
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView = findViewById(R.id.categoryImage);
        if (!categoryModel.getThumbnail().isEmpty()) {
            Glide.with(this).load(categoryModel.getThumbnail()).placeholder(R.drawable.placeholder).into(imageView);

        }


        //TapToChangeImage
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissionForReadExtertalStorage()) {
                    ShowFileChooser();
                }
                else {
                    requestStoragePermission();
                }

            }
        });

        if (!checkPermissionForReadExtertalStorage()) {
            requestStoragePermission();
        }

        EditText categoryName = findViewById(R.id.categoryNameET);


        categoryName.setText(categoryModel.getName());


        findViewById(R.id.addCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sCategory = categoryName.getText().toString().trim();

                if (sCategory.isEmpty()) {
                    Services.showCenterToast(EditCategoryActivity.this,"Enter Category Name");
                }
                else if (sCategory.isEmpty()) {
                    Services.showCenterToast(EditCategoryActivity.this,"Enter Quiz Quantity");
                }
                else {

                    categoryModel.name = sCategory;
                    ProgressHud.show(EditCategoryActivity.this,"Wait...");
                   if (isImageSelected) {
                       uploadImageOnFirebase(categoryModel);
                   }
                   else {
                       updateDataOnFirebase();
                   }

                }

            }
        });
    }

    public void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }


        ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);//If the user has denied the permission previously your code will come to this block

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    public void ShowFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {

            Uri filepath = data.getData();

            CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(filepath, new CropImageOptions());
            cropImageContractOptions.setAspectRatio(1,1);
            cropImageContractOptions.setFixAspectRatio(true);
            cropImageContractOptions.setOutputCompressFormat(Bitmap.CompressFormat.PNG);
            cropImageContractOptions.setOutputCompressQuality(60);
            cropImage.launch(cropImageContractOptions);
        }
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }


    private void uploadImageOnFirebase(CategoryModel categoryModel) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("CategoryPicture").child(categoryModel.getId()+ ".png");
        UploadTask uploadTask = storageReference.putFile(resultUri);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    ProgressHud.dialog.dismiss();
                    throw Objects.requireNonNull(task.getException());
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful()) {

                    categoryModel.thumbnail = String.valueOf(task.getResult());

                }

                updateDataOnFirebase();


            }
        });
    }

    private void updateDataOnFirebase(){
        FirebaseFirestore.getInstance().collection("Categories").document(categoryModel.getId()).set(categoryModel, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ProgressHud.dialog.dismiss();
                if (task.isSuccessful()) {
                    Services.showCenterToast(EditCategoryActivity.this,"Category Edited");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },2500);
                }
                else {
                    Services.showDialog(EditCategoryActivity.this,"ERROR",task.getException().getLocalizedMessage());
                }
            }
        });
    }
}
