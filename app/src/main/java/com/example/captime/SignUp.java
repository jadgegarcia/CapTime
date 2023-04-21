package com.example.captime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends AppCompatActivity {
    Button already_account, register_button;
    TextInputLayout full_name, username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        already_account = findViewById(R.id.btn_already_acc);
        register_button = findViewById(R.id.btn_reg);
        full_name = findViewById(R.id.et_fullname);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        already_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    // for email "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+"
    private Boolean validateName() {
        String val = full_name.getEditText().getText().toString();
        if(val.isEmpty()) {
            full_name.setError("Field cannot be empty");
            return false;
        } else {
            full_name.setError(null);
            full_name.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateUserName() {
        String val = username.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if(val.isBlank()) {
            username.setError("Field cannot be empty");
            return false;
        }else if(val.length() >= 15) {
            username.setError("Username too long");
            return false;
        }else if(!val.matches(noWhiteSpace)) {
            username.setError("White Spaces not allowed");
            return false;
        }else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=.])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password must have atleast 1 character, no white spaces and atleast 4 characters");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
    public void registerUser(View view) {

        if(!validateName() | !validateUserName() | !validatePassword()) {
            return;
        }

        //Get user information input
        String name = full_name.getEditText().getText().toString();
        String uname = username.getEditText().getText().toString();
        String pass = password.getEditText().getText().toString();
    }
}