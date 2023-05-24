package com.example.captime;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.captime.helperclass.DateHelperClass;
import com.example.captime.helperclass.EventHelperClass;

import com.example.captime.helperclass.TimeHelperClass;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AppListAdapter.AppItemClickListener
{
    //**********SQL********
    //private SQLiteDBHelper dbhandler;

    //Event Recyclerview
    RecyclerView eventRecycler;
    ArrayList<EventHelperClass> eventArrayList;
    EventAdapter eventAdapter;

    ProgressDialog progressDialog;
    DateHelperClass datehelper;
    TimeHelperClass timehelper;

    private String pickedColor;
    private String header_name, header_username, header_email;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    FloatingActionButton logout;
    DrawerLayout drawerLayout;
    LinearLayout background_layout;
    NavigationView navigationView;
    Toolbar toolbar;
    MaterialCalendarView calendar;
    TextView tvmonth, navdraw_name, navdraw_uname, navdraw_email;
    private LocalDate selectedDate;
    private LocalTime currentTime;
    FloatingActionButton freminder, fevent, ftimer, fadd;
    private Animation open, close;
    TextView lblremind, lblevent, lbltimer;
    boolean floatbool = true;
    Dialog dialogaddevent, dialogaddtimer;
    RelativeLayout addeventlayout;
    EditText addTitle, addDate, addTime, addColor, chooseApp;
    ImageView colorpickericon;
    private int iconColor;
    AutoCompleteTextView autoCompleteTextView, main_sort_event;
    String[] item = {"30 minutes", "1 hour", "2 hours", "4 hours", "8 hours", "1 day"};
    ArrayAdapter<String> adapterItem;
    TextInputLayout txtinptlyt;
    TextInputEditText noteEvent;
    ImageButton closeEvent;
    ExtendedFloatingActionButton saveEvent;

    @Override
    protected void onStart() {
        super.onStart();
        //EventChangeListener();
        updateListBaseDate(calendar.getCurrentDate(), 2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //EventChangeListener();
    }

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
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data....");
        progressDialog.show();


        main_sort_event = findViewById(R.id.event_menu);
        selectedDate = LocalDate.now();
        currentTime = LocalTime.now(ZoneId.of("Asia/Manila"));
        drawerLayout = findViewById(R.id.drawer_layout);
        background_layout = findViewById(R.id.background_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        tvmonth = findViewById(R.id.month);
        calendar = findViewById(R.id.calendarView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        logout = navigationView.findViewById(R.id.logout_btn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        View headerView = navigationView.getHeaderView(0);
        navdraw_name = headerView.findViewById(R.id.header_name);
        navdraw_uname = headerView.findViewById(R.id.header_username);
        navdraw_email = headerView.findViewById(R.id.header_email);
        checkUser(user);

        //************Tester Fetching data************
        Log.d("Name", "Name: " + navdraw_name.getText());
        Log.d("Username", "Username: " + navdraw_uname.getText());
        //********************************************

        //****************** fabs add animation ********************
        open = AnimationUtils.loadAnimation(this, R.anim.open_add);
        close = AnimationUtils.loadAnimation(this, R.anim.close_add);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        fadd = findViewById(R.id.add);
        ColorStateList colorWhite = ColorStateList.valueOf(Color.WHITE);
        fadd.setImageTintList(colorWhite);
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
                    // Apply rotation animation for opening
                    ObjectAnimator rotateOpen = ObjectAnimator.ofFloat(fadd, "rotation", 0f, 45f);
                    rotateOpen.setDuration(300);
                    rotateOpen.start();
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
                    ObjectAnimator rotateClose = ObjectAnimator.ofFloat(fadd, "rotation", 45f, 0f);
                    rotateClose.setDuration(300);
                    rotateClose.start();

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


        main_sort_event.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSort = parent.getItemAtPosition(position).toString();

                // Perform your desired action based on the selected sort
                if (selectedSort.equals("Month")) {
                    // Edit something for option 1
                    updateListBaseDate(calendar.getCurrentDate(), 2);
                } else if (selectedSort.equals("Year")) {
                    updateListBaseDate(calendar.getCurrentDate(), 3);

                }
                calendar.setSelectedDate(LocalDate.now());
            }
        });

        calendar.setOnMonthChangedListener(new OnMonthChangedListener() {

            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                tvmonth.setText(getMonthtoString(date.getMonth()) +" "+ date.getYear());
                eventAdapter.clear();
                updateListBaseDate(date, 2);

            }
        });
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {


            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                tvmonth.setText(getMonthtoString(date.getMonth()) +" "+ date.getYear());
                eventAdapter.clear();
                main_sort_event.getText().clear();
                main_sort_event.setHint("Sort by");
                updateListBaseDate(date, 1);
            }

        });
        eventRecycler = findViewById(R.id.eventRecyclerView);

        EventChangeListener();


        //************  ADD EVENT   ***************
        View view = getLayoutInflater().inflate(R.layout.add_event, null);
        dialogaddevent = new Dialog(this);
        dialogaddevent.setContentView(view);
        closeEvent = dialogaddevent.findViewById(R.id.close_event);
        saveEvent = dialogaddevent.findViewById(R.id.save_event);
        noteEvent = dialogaddevent.findViewById(R.id.note_event);
        addTitle = dialogaddevent.findViewById(R.id.add_title);
        addDate = dialogaddevent.findViewById(R.id.add_date);
        addTime = dialogaddevent.findViewById(R.id.add_time);
        addColor =dialogaddevent.findViewById(R.id.add_color);
        colorpickericon = dialogaddevent.findViewById(R.id.add_color_icon);
        iconColor = getResources().getColor(R.color.captime_color);
        colorpickericon.setColorFilter(iconColor);

        //************* ADD TIMER *************
        View view2 = getLayoutInflater().inflate(R.layout.add_timer, null);
        dialogaddtimer = new Dialog(this);
        dialogaddtimer.setContentView(view2);
        chooseApp = dialogaddtimer.findViewById(R.id.choose_app);

        //******************Store event *****************

        // Set the background drawable to a custom shape with curved edges
        dialogaddevent.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialogaddtimer.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        //***************Date Picker************************

        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), "Material_Date_Picker");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTimeInMillis((Long) selection);
                        addDate.setText(datePicker.getHeaderText());
                        datehelper = new DateHelperClass(calendar1.get(Calendar.DAY_OF_MONTH),calendar1.get(Calendar.MONTH) + 1,calendar1.get(Calendar.YEAR));

                        //**********Tester************
                            Log.d("Date", "Date: " + datehelper.toString());
                        //**************


                    }
                });
            }
        });


        //***************Time Picker************************

        MaterialTimePicker timePicker =
                new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(currentTime.getHour())
                        .setMinute(currentTime.getMinute())
                        .setTitleText("Select Time")
                        .build();
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(getSupportFragmentManager(), "Material_Time_Picker");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timehelper = new TimeHelperClass(timePicker.getMinute(), timePicker.getHour());

                        if(timehelper.getMinute() == 0) {
                            addTime.setText(timehelper.getHour()+ ":00");
                        } else {
                            String hr_temp = String.format("%d", timehelper.getHour());
                            String min_temp = String.format("%d", timehelper.getMinute());
                            if(timehelper.getHour() < 10) {
                                hr_temp = String.format("0%d", timehelper.getMinute());
                            }
                            if(timehelper.getMinute() < 10) {
                                min_temp = String.format("0%d", timehelper.getMinute());
                            }
                            addTime.setText(timehelper.getHour()+ ":" + timehelper.getMinute());
                        }
                        //**********Tester************
                        Log.d("Time", "Time: " + timehelper.toString());
                        //*****************************

                    }
                });
            }
        });

        //***************Color Picker************************
        addColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder.with(MainActivity.this)
                        .setTitle("Choose Color")
                        .initialColor(iconColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                Toast.makeText(MainActivity.this, "Selected color: #" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("OK", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                                iconColor = lastSelectedColor;
                                colorpickericon.setColorFilter(iconColor);
                                addColor.setText("#" + Integer.toHexString(lastSelectedColor));
                                pickedColor = "#" + Integer.toHexString(lastSelectedColor);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).build().show();
            }
        });

        closeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeEventDialog();
            }
        });

        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventHelperClass event = new EventHelperClass(user.getEmail(), addTitle.getText().toString(),datehelper.getDay(), datehelper.getMonth(),
                        datehelper.getYear(), timehelper.getHour(), timehelper.getMinute(), addColor.getText().toString(), noteEvent.getText().toString());
                storeEventToDB(event);

                closeEventDialog();
//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });


        // ******* on click of add fabs ********
        fevent.setOnClickListener(this);
        ftimer.setOnClickListener(this);

        txtinptlyt = dialogaddtimer.findViewById(R.id.txtinputduration);
        txtinptlyt.setHintTextAppearance(R.style.CustomHintTextAppearance);

        autoCompleteTextView = dialogaddtimer.findViewById(R.id.auto_complete_txt);
        adapterItem = new ArrayAdapter<String>(this, R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItem);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //txtinptlyt.setHintEnabled(false);
                String item = parent.getItemAtPosition(position).toString();
                //autoCompleteTextView.setText(item);
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
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
            case R.id.nav_timetable:
                intent = new Intent(MainActivity.this, Reminder.class);
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

    @Override
    public void onClick(View v) {
        Window window;
        switch (v.getId()) {
            case R.id.timer:
                clearDialogevent();
                window = dialogaddtimer.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.gravity = Gravity.TOP; // Adjust the gravity as needed
                    window.setAttributes(layoutParams);
                    //window.setBackgroundDrawableResource(android.R.color.transparent); // Remove default dialog background
                }
                dialogaddtimer.show();
                break;
            case R.id.event:
                window = dialogaddevent.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.gravity = Gravity.TOP; // Adjust the gravity as needed
                    window.setAttributes(layoutParams);
                    //window.setBackgroundDrawableResource(android.R.color.transparent); // Remove default dialog background
                }
                dialogaddevent.show();
                break;
            case R.id.remind:
                break;


        }
    }

    public void showAppPickerDialog(View view) {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(0);

        // Inside your activity or fragment
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_app_list);

        RecyclerView recyclerView = dialog.findViewById(R.id.app_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




        //AlertDialog dialogbtn = buildAppListDialog();
        AppListAdapter adapter = new AppListAdapter(installedApps, chooseApp);
        //adapter.setEtchosenApp(chooseApp);
        recyclerView.setAdapter(adapter);

        dialog.show();

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnOk = dialog.findViewById(R.id.btn_ok);

        // Set click listener for the Cancel button
        btnCancel.setOnClickListener(v -> {
            // Close the dialog without updating the EditText
            chooseApp.setText("");
            dialog.dismiss();
        });

        // Set click listener for the OK button
        btnOk.setOnClickListener(v -> {
            // Get the selected app from the adapter
            ApplicationInfo selectedApp = adapter.getSelectedApp();

            if (selectedApp != null) {
                // Get the app name
                String appName = selectedApp.loadLabel(getPackageManager()).toString();

                // Update the EditText with the selected app name
                chooseApp.setText(appName);
            }

            // Close the dialog
            dialog.dismiss();
        });
    }

    @Override
    public void onAppItemClick(ApplicationInfo appInfo) {
        String appName = appInfo.loadLabel(getPackageManager()).toString();
        chooseApp.setText(appName);
    }

    public void checkUser(FirebaseUser u) {
        if(u == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            //navdraw_name.setText(user.getDisplayName());
            navdraw_email.setText(u.getEmail());
            header_email = u.getEmail();
            fetchFireStore(u.getEmail());
            Log.d("Email", "Email: " + u.getEmail());
            //navdraw_uname.setText(user.getUid());
        }
    }

    private void fetchFireStore(String header_email) {
        //Query query = db.collection("users").whereEqualTo("email", header_email);
        db.collection("users")
                .whereEqualTo("email", header_email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //header_name = document.getString("email");
                                navdraw_name.setText(document.getString("name"));
                                navdraw_uname.setText(document.getString("username"));
                                header_name = document.getString("name");
                                header_username = document.getString("username");

                            }
                        } else {
                            Log.d("Error", "No record returned");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void clearDialogevent() {
        addDate.getText().clear();
        addTitle.getText().clear();
        addTime.getText().clear();
        addColor.getText().clear();
        noteEvent.getText().clear();
        colorpickericon.setColorFilter(R.color.captime_color);
    }

    private void closeEventDialog() {
        addDate.getText().clear();
        addTitle.getText().clear();
        addTime.getText().clear();
        addColor.getText().clear();
        noteEvent.getText().clear();
        colorpickericon.setColorFilter(R.color.captime_color);
        dialogaddevent.dismiss();
    }

    private void EventChangeListener() {
        db.collection("events").whereEqualTo("email", user.getEmail()).orderBy("year", Query.Direction.ASCENDING)
                .orderBy("month", Query.Direction.ASCENDING).orderBy("day", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        //eventArrayList.clear();
                        eventArrayList = new ArrayList<>();
                        eventArrayList.addAll(value.toObjects(EventHelperClass.class));

                        eventRecycler.setHasFixedSize(true);
                        eventRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                        eventAdapter = new EventAdapter(MainActivity.this, eventArrayList, getSupportFragmentManager());
                        eventRecycler.setAdapter(eventAdapter);

                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public void storeEventToDB(EventHelperClass e) {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("title", e.getTitle());
        eventMap.put("email", e.getEmail());
        eventMap.put("day", e.getDay());
        eventMap.put("month", e.getMonth());
        eventMap.put("year", e.getYear());
        eventMap.put("hour", e.getHour());
        eventMap.put("minute", e.getMinute());
        eventMap.put("color", e.getColor());
        eventMap.put("note", e.getNote());

        db.collection("events").add(eventMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Event", toString());

                    }
                });
    }


    private void updateListBaseDate(CalendarDay date, int n) {

        if(n == 1) {
            db.collection("events").whereEqualTo("email", user.getEmail()).whereEqualTo("year", date.getYear()).whereEqualTo("month", date.getMonth()).whereEqualTo("day", date.getDay())
                    .orderBy("day", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null) {
                                if(progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Log.e("Firestore error", error.getMessage());
                                return;
                            }
                            if(value.isEmpty()) {
                                eventAdapter.clear();
                            }
                            //eventArrayList.clear();
                            eventArrayList = new ArrayList<>();
                            eventArrayList.addAll(value.toObjects(EventHelperClass.class));

                            eventRecycler.setHasFixedSize(true);
                            eventRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                            eventAdapter = new EventAdapter(MainActivity.this, eventArrayList, getSupportFragmentManager());
                            eventRecycler.setAdapter(eventAdapter);

                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    });
        } else if(n == 2) {
            db.collection("events").whereEqualTo("email", user.getEmail()).whereEqualTo("year", date.getYear()).whereEqualTo("month", date.getMonth())
                    .orderBy("month", Query.Direction.ASCENDING).orderBy("day", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null) {
                                if(progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Log.e("Firestore error", error.getMessage());
                                return;
                            }
                            //eventArrayList.clear();
                            eventArrayList = new ArrayList<>();
                            eventArrayList.addAll(value.toObjects(EventHelperClass.class));

                            eventRecycler.setHasFixedSize(true);
                            eventRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                            eventAdapter = new EventAdapter(MainActivity.this, eventArrayList, getSupportFragmentManager());
                            eventRecycler.setAdapter(eventAdapter);

                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    });
        } else if(n == 3) {
            db.collection("events").whereEqualTo("email", user.getEmail()).whereEqualTo("year", date.getYear()).orderBy("year", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null) {
                                if(progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Log.e("Firestore error", error.getMessage());
                                return;
                            }
                            //eventArrayList.clear();
                            eventArrayList = new ArrayList<>();
                            eventArrayList.addAll(value.toObjects(EventHelperClass.class));

                            eventRecycler.setHasFixedSize(true);
                            eventRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                            eventAdapter = new EventAdapter(MainActivity.this, eventArrayList, getSupportFragmentManager());
                            eventRecycler.setAdapter(eventAdapter);

                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    });
        } else {
            db.collection("events").whereEqualTo("email", user.getEmail()).orderBy("year", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null) {
                                if(progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Log.e("Firestore error", error.getMessage());
                                return;
                            }
                            //eventArrayList.clear();
                            eventArrayList = new ArrayList<>();
                            eventArrayList.addAll(value.toObjects(EventHelperClass.class));

                            eventRecycler.setHasFixedSize(true);
                            eventRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                            eventAdapter = new EventAdapter(MainActivity.this, eventArrayList, getSupportFragmentManager());
                            eventRecycler.setAdapter(eventAdapter);

                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    });
        }


    }


}

