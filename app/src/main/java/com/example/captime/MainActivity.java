package com.example.captime;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.threeten.bp.LocalDate;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    DrawerLayout drawerLayout;
    LinearLayout background_layout;
    NavigationView navigationView;
    Toolbar toolbar;
    MaterialCalendarView calendar;
    TextView tvmonth;
    private LocalDate selectedDate;
    FloatingActionButton freminder, fevent, ftimer, fadd;
    TextView lblremind, lblevent, lbltimer;
    boolean floatbool = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (floatbool == false) {

                Rect outRect = new Rect();
                fadd.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    ftimer.hide();
                    fevent.hide();
                    freminder.hide();
                    lblremind.setVisibility(View.GONE);
                    lblevent.setVisibility(View.GONE);
                    lbltimer.setVisibility(View.GONE);
                    floatbool = true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        background_layout.setRenderEffect(null);
                    }
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedDate = LocalDate.now();
        drawerLayout = findViewById(R.id.drawer_layout);
        background_layout = findViewById(R.id.background_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        tvmonth = findViewById(R.id.month);
        calendar = findViewById(R.id.calendarView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        fadd = findViewById(R.id.add);
        ftimer = findViewById(R.id.timer);
        fevent = findViewById(R.id.event);
        freminder = findViewById(R.id.remind);
        lblremind = findViewById(R.id.tvremind);
        lblevent = findViewById(R.id.tvevent);
        lbltimer = findViewById(R.id.tvtimer);

        lblremind.setVisibility(View.GONE);
        lblevent.setVisibility(View.GONE);
        lbltimer.setVisibility(View.GONE);
        fadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(floatbool) {
                    ftimer.show();
                    fevent.show();
                    freminder.show();
                    lblremind.setVisibility(View.VISIBLE);
                    lblevent.setVisibility(View.VISIBLE);
                    lbltimer.setVisibility(View.VISIBLE);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        background_layout.setRenderEffect(RenderEffect.createBlurEffect(30, 30, Shader.TileMode.MIRROR));
                    }
                    floatbool = false;
                } else {
                    ftimer.hide();
                    fevent.hide();
                    freminder.hide();
                    lblremind.setVisibility(View.GONE);
                    lblevent.setVisibility(View.GONE);
                    lbltimer.setVisibility(View.GONE);
                    floatbool = true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        background_layout.setRenderEffect(null);
                    }
                }
            }
        });


        calendar.setTopbarVisible(false);
        calendar.setSelectedDate(selectedDate);
        tvmonth.setText(getMonthtoString(calendar.getSelectedDate().getMonth())+ " " + calendar.getSelectedDate().getYear());


        //**********************Navigation Drawer Menu*************************
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                tvmonth.setText(getMonthtoString(date.getMonth()) +" "+ date.getYear());
            }
        });
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                tvmonth.setText(getMonthtoString(date.getMonth()) +" "+ date.getYear());
            }
        });



    }





    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Toast.makeText(this, "Home is Clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_calendar:
                Toast.makeText(this, "Calendar is Clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_timetable:
                intent = new Intent(MainActivity.this, TimeTableActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_task:
                intent = new Intent(MainActivity.this, TaskActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getMonthtoString(int m) {
        switch(m) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return null;
    }

}