package com.example.kiitanonchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    Button dialog_button;
    EditText dialog_editText;
    AlertDialog dialog;
    int i = 0;
    int num =0;
    int banNum = 0;
    SharedPreferences sharedPreferences;
    FirebaseDatabase db;
    DatabaseReference nodeOfUser;
    TextView uniqueIdTv;
    String uniqueId;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        uniqueId = Settings.Secure.getString(this.getContentResolver() , Settings.Secure.ANDROID_ID);

        getSharedPreferences("version", 0).edit().putString("version", "1").apply();
        if(isConnectedToInternet(getApplicationContext())){
            Toast.makeText(context, "No network connect. Please connect and try again.", Toast.LENGTH_LONG).show();
        }
        else{
            isBanned(uniqueId);
        }
    }
    private void isBanned(String uniqueId) {
        FirebaseDatabase db= FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference().child("Banned");
        reference.orderByChild("bannedUser").equalTo(uniqueId).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setContentView(R.layout.banned);
                    uniqueIdTv = findViewById(R.id.uniqueId);
                    uniqueIdTv.setText(uniqueId);               }
                else{
                    isLoggedIn();
                    //DO THESE IF NOT LOGGED IN
                    if (i == 0) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        nodeOfUser = db.getReference().child("AllUsers");

//          SHARED PREFERENCES TO SET COLOR OF THE USERNAME IN CHAT

                        sharedPreferences = context.getSharedPreferences(
                                "COLOR", Context.MODE_PRIVATE);
                        String[] arr = {"R.color.text5", "R.color.text1", "R.color.text2", "R.color.text3", "R.color.text4"};
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Random rand = new Random();
                        int randomNum = rand.nextInt(5);
                        editor.putString("COLOR", arr[randomNum]);
                        editor.apply();

                        Objects.requireNonNull(getSupportActionBar()).hide();
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        setContentView(R.layout.main_activity);

                        AlertDialog.Builder alert;
                        alert = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.nickname_dialog, null);
                        dialog_editText = view.findViewById(R.id.dialog_editText);
                        dialog_button = view.findViewById(R.id.dialog_button);
                        alert.setView(view);
                        alert.setCancelable(false);
                        dialog = alert.create();
                        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        dialog_button.setOnClickListener(v -> {
                            if (isConnectedToInternet(getApplicationContext())) {
                                Toast.makeText(MainActivity.this, "No internet connection.", Toast.LENGTH_LONG).show();
                            } else {
                                String name = dialog_editText.getText().toString().trim();
                                if (isUsernameOk(name)) {
                                    userExists(name, num);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error: No net connection or DataBase Error. Try again Later", Toast.LENGTH_LONG).show();
            }
        });
    }

    //CHECK IF USER EXISTS OR NOT
    private void userExists(String allUsername , int num) {
        nodeOfUser.orderByChild("allUsername").equalTo(allUsername.toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Nickname already taken. Try another.", Toast.LENGTH_LONG).show();
                } else {
                    if(num ==1){
                        getSharedPreferences("Current_Username", 0).edit().putString("username", "admin").apply();
                        getSharedPreferences("LogInCheck", MODE_PRIVATE).edit().putBoolean("isLoggedIn", true).apply();
                        Intent intent = new Intent(MainActivity.this, MainChat.class);
                        startActivity(intent);
                    }
                    else {
                        AllUsers AllUsersObj = new AllUsers(dialog_editText.getText().toString().toLowerCase().trim());
                        nodeOfUser.push().setValue(AllUsersObj);
                        getSharedPreferences("Current_Username", 0).edit().putString("username", dialog_editText.getText().toString().toLowerCase().trim()).apply();
                        getSharedPreferences("LogInCheck", MODE_PRIVATE).edit().putBoolean("isLoggedIn", true).apply();
                        Intent intent = new Intent(MainActivity.this, MainChat.class);
                        startActivity(intent);
                    }
                    dialog.dismiss();
                    MainActivity.this.finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    //CHECK IF THE USER IS LOGGED IN OR NOT
    private void isLoggedIn() {
        boolean isLoggedIn = getSharedPreferences("LogInCheck", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            i = 1;
            Intent intent = new Intent(MainActivity.this, MainChat.class);
            startActivity(intent);
            finish();
        }
    }

    //CHECK IF THE USER IS CONNECTED TO THE INTERNET OR NOT
    private boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo == null || !netInfo.isConnectedOrConnecting();
    }

    //CHECK USERNAME
    private boolean isUsernameOk(String username){
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+");
        if(username.equalsIgnoreCase("admin")){
            Toast.makeText(this, "The username is reserved. Try another.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(username.equals("$admin")){
            Toast.makeText(this, "Logged in as admin", Toast.LENGTH_SHORT).show();
            num = 1;
            return true;
        }
        if (username.length()>16){
            Toast.makeText(this, "Too long nickname. Should be less than 16 characters.", Toast.LENGTH_LONG).show();
            return false;
        }


        if(username.length()<3){
            Toast.makeText(this, "Too small nickname. Should be more than 2 characters.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!pattern.matcher(username).matches()){

            Toast.makeText(this, "Invalid nickname. Nicknames can only contain numbers and letters", Toast.LENGTH_LONG).show();
        return false;}
        return true;
    }
}