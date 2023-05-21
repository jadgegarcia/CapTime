package com.example.captime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

public class Login extends AppCompatActivity {
    Button login, signup;

    TextView captime, tagline, header, note;
    TextInputLayout e_email, pass;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            Intent intent = new Intent(Login.this, MainActivity.class);
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
        setContentView(R.layout.activity_login);

        //Hooks
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        login = findViewById(R.id.btn_login);
        signup = findViewById(R.id.btn_signup);
        captime = findViewById(R.id.tv_captime_text);
        tagline = findViewById(R.id.tv_tagline);
        header = findViewById(R.id.tv_login);
        note = findViewById(R.id.tv_login_slogan);
        e_email = findViewById(R.id.et_email);
        pass = findViewById(R.id.et_pass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = e_email.getEditText().getText().toString();
                password = pass.getEditText().getText().toString();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplicationContext(), "Login Successfully",
                                            Toast.LENGTH_SHORT).show();
                                    db.collection("users")
                                            .whereEqualTo("email", email)
                                            .whereEqualTo("password", password)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d("login", document.getId() + " => " + document.getData());
                                                        }
                                                    } else {
                                                        Log.w("Login", "Error getting documents.", task.getException());
                                                    }
                                                }
                                            });

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
                pairs[4] = new Pair<View, String>(e_email, "username_trans");
                pairs[5] = new Pair<View, String>(pass, "password_trans");
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