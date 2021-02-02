package com.example.kiitanonchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class ActivityConfirm extends AppCompatActivity {
    FloatingActionButton fab;
    ImageView confirm_image;
    Uri myUri;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        Objects.requireNonNull(getSupportActionBar()).hide();
        fab = findViewById(R.id.fab);
        confirm_image = findViewById(R.id.confirm_image);
        Bundle extras = getIntent().getExtras();
        myUri = Uri.parse(extras.getString("imageUri"));
        confirm_image.setImageURI(myUri);
        fab.setOnClickListener(v -> {
            uploadToFireBase();
        });
    }

    private void uploadToFireBase() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference uploader = storage.getReference("Image1" + new Random().nextInt(50));
        uploader.putFile(myUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                dialog.dismiss();
                                String username = getSharedPreferences("Current_Username", 0).getString("username", "Anonymous");
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                                String time = simpleDateFormat.format(c.getTime());
                                sharedPreferences = getSharedPreferences("COLOR", Context.MODE_PRIVATE);
                                String userColor = sharedPreferences.getString("COLOR", "R.color.text1");
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference root = db.getReference("AllChat").push();
                                model obj = new model("$null$" , username , userColor , time , uri.toString() );
                                root.setValue(obj);
                                finish();
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        dialog.setMessage("Sending : " + (int) percent + " %");
                    }
                });
    }
}