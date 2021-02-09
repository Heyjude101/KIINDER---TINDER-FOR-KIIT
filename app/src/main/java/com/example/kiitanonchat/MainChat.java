package com.example.kiitanonchat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainChat extends AppCompatActivity
{

    //uploadButton is what you have to search for.
    Bitmap bitmap;
    ActionBar actionBar;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    EditText et1;
    String versionC;
    DatabaseReference reference;
    DatabaseReference referenceRealTime;
    ImageButton b1, uploadButton;
    Button dialog_button_update;
    LinearLayoutManager mLayoutManager;
    myAdapter adapter;
    TextView errorMessage , dialog_TextView_update;
    ProgressBar pb;
    FirebaseDatabase db;
    model obj;
    String url;
    AlertDialog dialog;
    Uri filepath;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Kiit Chat");
        db = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        b1 = findViewById(R.id.b1);
        uploadButton = findViewById(R.id.uploadButton);
        et1 = findViewById(R.id.et1);
        b1.setEnabled(false);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        pb = findViewById(R.id.simpleProgressBar);
        errorMessage= findViewById(R.id.errorMessage);
        pb.setVisibility(View.VISIBLE);


        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("AllChat"), model.class)
                        .build();
        adapter = new myAdapter(options);
        recyclerView.setAdapter(adapter);

        if(!isConnectedToInternet(getApplicationContext())){
            errorMessage.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference node = db.getReference("AllChat");
        DatabaseReference nodeU = db.getReference("AllUsers");


        FirebaseDatabase.getInstance().getReference().child("AllChat").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pb.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(MainChat.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
                }
        });


        //DISABLE SEND BUTTON WHEN EMPTY
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                b1.setEnabled(s.toString().trim().length() > 0);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //SEND BUTTON CLICK HANDLER
        b1.setOnClickListener(v -> {

            if(isConnectedToInternet(getApplicationContext())) {
                String val = et1.getText().toString();
                if (val.contains("$clearChat$")) {
                    node.removeValue();
                    Toast.makeText(this, "All messages Deleted.", Toast.LENGTH_SHORT).show();
                }
                else if(val.contains("$clearUser$")) {
                    nodeU.removeValue();
                    Toast.makeText(this, "All users Deleted.", Toast.LENGTH_SHORT).show();
                }
                else {
                    sharedPreferences = getSharedPreferences("COLOR", Context.MODE_PRIVATE);
                    String username = getSharedPreferences("Current_Username", 0).getString("username", "Anonymous");
                    DatabaseReference newVal = node.push();
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                    String time = simpleDateFormat.format(c.getTime());
                    String userColor = sharedPreferences.getString("COLOR", "R.color.text1");
                    obj = new model(val, username, userColor, time , "$null$");
                    newVal.setValue(obj);


                }
                et1.setText("");
            }
            else{
                Toast.makeText(this, "Unable to send message. Check Internet connection.", Toast.LENGTH_SHORT).show();
            }
        });

        //HELP WITH THE SCROLL ISSUE
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                int lastVisiblePosition =
                        mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });
    }

    //OTHER FUNCTIONS

    private boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    //MENU OPTION
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main , menu);
        int positionOfMenuItem = 3;
        MenuItem item = menu.getItem(positionOfMenuItem);
        SpannableString s = new SpannableString("DUMP THIS ACCOUNT");
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        item.setTitle(s);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.confess){
            startActivity(new Intent(getApplicationContext() , ConfessActivity.class));
        }
        if(itemId == R.id.about){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainChat.this);
            builder.setView(R.layout.about_dialog)
                    .setTitle("How to use?")
                    .setCancelable(true)
                    .setNeutralButton("Ok" , null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if(itemId == R.id.logout){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainChat.this);
            builder1.setView(R.layout.logout_dialog)
                    .setTitle("Are you sure?")
                    .setCancelable(true)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SharedPreferences preferences =getSharedPreferences("LogInCheck", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            SharedPreferences colorPrefs =getSharedPreferences("COLOR", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorC = colorPrefs.edit();
                            editorC.clear();
                            editorC.apply();
                            SharedPreferences usernamePrefs =getSharedPreferences("COLOR", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorUserPrefs = usernamePrefs.edit();
                            editorUserPrefs.clear();
                            editorUserPrefs.apply();

                            finish();
                            Toast.makeText(MainChat.this, "Account deleted and exited the app", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNeutralButton("Cancel" , null);
            AlertDialog dialog = builder1.create();
            dialog.show();
        }
        if(itemId == R.id.update){
            AlertDialog.Builder alert;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                alert = new AlertDialog.Builder(this);
            }
            setTheDesignRealTime();
            versionC= getSharedPreferences("version" , 0).getString("version" , "0");
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.update_dialog, null);
            dialog_TextView_update = view.findViewById(R.id.dialog_TextView_update);
            dialog_button_update = view.findViewById(R.id.dialog_button_update);
            alert.setView(view);
            isSameVersion(versionC);
            alert.setCancelable(true);
            dialog = alert.create();
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
            dialog_button_update.setOnClickListener(v -> {
                if(isConnectedToInternet(getApplicationContext())) {
                    Uri uri = Uri.parse(url);
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
                else {
                    Toast.makeText(MainChat.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return true; }


    //ACTIVITY LIFECYCLE
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Exited the App", Toast.LENGTH_SHORT).show();
    }


    //Version check
    private void isSameVersion(String versionC) {
        reference = db.getReference().child("Version");
        reference.orderByChild("version").equalTo(versionC).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    dialog_TextView_update.setText("No updates Available at the moment. Please check back later.");

                }
                else{
                    dialog_TextView_update.setText("Updates are Available. Please click on download button to proceed.");
                    dialog_button_update.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void setTheDesignRealTime(){
        referenceRealTime = db.getReference().child("Realtime");
        referenceRealTime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()) {
                    url = (String) snap.child("downloadUrl").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            assert data != null;
            if (data.getData() != null) {
                filepath = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(filepath);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (Exception ex) {
                }
                Intent intent = new Intent(MainChat.this , ActivityConfirm.class);
                intent.putExtra("imageUri" , filepath.toString());
                startActivity(intent);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}