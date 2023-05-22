package com.example.captime;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    Button already_account, register_button;
    TextInputLayout full_name, uname, pass, mail;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DatabaseReference reference;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(SignUp.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        //this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);



        already_account = findViewById(R.id.btn_already_acc);
        register_button = findViewById(R.id.btn_reg);
        full_name = findViewById(R.id.et_fullname);
        uname = findViewById(R.id.et_username);
        pass = findViewById(R.id.et_password);
        mail = findViewById(R.id.et_mail);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        already_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateName() | !validateUserName() | !validatePassword()) {
                    return;
                }

                //Get user information input
                String fullname = full_name.getEditText().getText().toString();
                String username = uname.getEditText().getText().toString();
                String password = pass.getEditText().getText().toString();
                String email_add = mail.getEditText().getText().toString();
                Map<String, Object> user = new HashMap<>();

                user.put("email", email_add);
                user.put("password", password);
                user.put("name", fullname);
                user.put("username", username);

                mAuth.createUserWithEmailAndPassword(email_add, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(SignUp.this, "Successfully Registered.",
                                            Toast.LENGTH_SHORT).show();

                                    db.collection("users")
                                            .add(user)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d("Signup", "DocumentSnapshot added with ID: " + documentReference.getId());

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("Signup", "Error adding document", e);
                                                    Toast.makeText(getApplicationContext(), "Firestore error", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    Intent intent = new Intent(SignUp.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.e("SignUp", "Authentication failed: " + task.getException().getMessage());
                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
        String val = uname.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if(val.isBlank()) {
            uname.setError("Field cannot be empty");
            return false;
        }else if(val.length() >= 15) {
            uname.setError("Username too long");
            return false;
        }else if(!val.matches(noWhiteSpace)) {
            uname.setError("White Spaces not allowed");
            return false;
        }else {
            uname.setError(null);
            uname.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = pass.getEditText().getText().toString();
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
            pass.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            pass.setError("Password must have atleast 1 special character, no white spaces and atleast 4 characters");
            return false;
        } else {
            pass.setError(null);
            pass.setErrorEnabled(false);
            return true;
        }
    }


}