package com.example.captime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {
    Button login, signup;

    TextView captime, tagline, header, note;
    TextInputLayout username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Hooks
        login = findViewById(R.id.btn_login);
        signup = findViewById(R.id.btn_signup);
        captime = findViewById(R.id.tv_captime_text);
        tagline = findViewById(R.id.tv_tagline);
        header = findViewById(R.id.tv_login);
        note = findViewById(R.id.tv_login_slogan);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_pass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);


                Pair[] pairs = new Pair[8];

                pairs[0] = new Pair<View, String>(captime, "captime_text");
                pairs[1] = new Pair<View, String>(tagline, "captime_tagline");
                pairs[2] = new Pair<View, String>(header, "header_trans");
                pairs[3] = new Pair<View, String>(note, "note_trans");
                pairs[4] = new Pair<View, String>(username, "username_trans");
                pairs[5] = new Pair<View, String>(password, "password_trans");
                pairs[6] = new Pair<View, String>(login, "button_trans");
                pairs[7] = new Pair<View, String>(signup, "login_signup_trans");

                //Will only run if API is Lollipop and up
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }
        });


    }
}