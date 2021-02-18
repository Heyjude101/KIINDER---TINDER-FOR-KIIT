package com.example.kiitanonchat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConfessActivity extends AppCompatActivity {
    ActionBar actionBar;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    EditText et1;
    ImageButton b1, uploadButton;
    LinearLayoutManager mLayoutManager;
    ConfessAdapter adapter;
    TextView errorMessage;
    ProgressBar pb;
    FirebaseDatabase db;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confess);

        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("KIIT Confessions");
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext() , R.drawable.toolbarbg));
        db = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.recyclerViewc);
        b1 = findViewById(R.id.b1c);
        uploadButton = findViewById(R.id.uploadButton);
        et1 = findViewById(R.id.et1c);
        b1.setEnabled(false);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        pb = findViewById(R.id.simpleProgressBarc);
        errorMessage= findViewById(R.id.errorMessagec);
        pb.setVisibility(View.VISIBLE);


        FirebaseRecyclerOptions<modelc> optionsc =
                new FirebaseRecyclerOptions.Builder<modelc>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Confessions"), modelc.class)
                        .build();
        adapter = new ConfessAdapter(optionsc);
        recyclerView.setAdapter(adapter);

        if(!isConnectedToInternet(getApplicationContext())){
            errorMessage.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference node = db.getReference("ReviewConfessions");

        //Control the Loading progress bar
        FirebaseDatabase.getInstance().getReference().child("Confessions").addListenerForSingleValueEvent(new ValueEventListener() {
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

                AlertDialog.Builder builder = new AlertDialog.Builder(ConfessActivity.this);
                builder
                        .setTitle("Do you want to proceed?")
                        .setMessage("Your confession , \"" + val +"\" will be sent for review before it's approved. This is a protective measure. Press Ok to send for approval.")
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPreferences = getSharedPreferences("COLOR", Context.MODE_PRIVATE);
                                String username = getSharedPreferences("Current_Username", 0).getString("username", "Anonymous");
                                DatabaseReference newVal = node.push();
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                                String time = simpleDateFormat.format(c.getTime());
                                String userColor = sharedPreferences.getString("COLOR", "R.color.text1");
                                reviewConf obj = new reviewConf(val, username, userColor, time);
                                newVal.setValue(obj);
                                et1.setText("");
                                Toast.makeText(ConfessActivity.this, "Sent.", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ConfessActivity.this, "Confession was not sent.", Toast.LENGTH_LONG).show();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

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

    private boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuc , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.rules){
            AlertDialog dialog;
                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    alert = new AlertDialog.Builder(this);
                }
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.rules_dialog, null);
                alert.setView(view);
                alert.setCancelable(true);
                dialog = alert.create();
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        return true;
    }
}