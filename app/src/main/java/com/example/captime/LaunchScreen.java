package com.example.captime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class LaunchScreen extends AppCompatActivity {

    private static int LAUNCH_SCREEN = 3000;

    //Animation Variables
    Animation topAnim, bottomAnim;

    //Launch Page Variables
    TextView captime, tagline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch_screen);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        captime = findViewById(R.id.tv_captime);
        tagline = findViewById(R.id.tv_tagline);

        captime.setAnimation(topAnim);
        tagline.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LaunchScreen.this, MainActivity.class);
                startActivity(intent);
            }
        }, LAUNCH_SCREEN);
    }
}