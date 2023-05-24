package com.example.captime;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    Context context;
    ArrayList<EventHelperClass> eventArrayList;
    private boolean  showButtons = false;
    LayoutInflater inflater;
    Dialog updateEvent;
    int pos;
    private DateHelperClass datehelper;
    private TimeHelperClass timeHelperClass;
    private FragmentManager fragmentManager;
    private String pickedColor;

    FirebaseFirestore  db;

    public EventAdapter(Context context, ArrayList<EventHelperClass> eventArrayList, FragmentManager fragmentManager) {
        db = FirebaseFirestore.getInstance();
        timeHelperClass = new TimeHelperClass();
        datehelper = new DateHelperClass();
        this.context = context;
        this.fragmentManager = fragmentManager;
        if (eventArrayList != null) {
            this.eventArrayList = eventArrayList;
        } else {
            this.eventArrayList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EventHelperClass event =  eventArrayList.get(position);
        holder.title.setText(event.getTitle());
        holder.note.setText(event.getNote());

        int color = Color.parseColor(event.getColor());
        holder.cardView.setCardBackgroundColor(color);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopupMenu(v, event, position);
                return false;
            }
        });

    }

    private void showPopupMenu(View view, EventHelperClass event, int pos) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.popup_event_recycler_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_edit:
                        // Handle edit option
                        openUpdateActivity(event, pos);
                        return true;
                    case R.id.menu_delete:
                        // Handle delete option
                        deleteItem(event);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void openUpdateActivity(EventHelperClass event, int pos) {
        timeHelperClass.setHour(event.getHour());
        timeHelperClass.setMinute(event.getMinute());
        datehelper.setDay(event.getDay());
        datehelper.setMonth(event.getMonth());
        datehelper.setYear(event.getYear());
        EventHelperClass prev = event;
        updateEvent = new Dialog(context);
        updateEvent.setContentView(R.layout.update_event);
        updateEvent.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        // Show the dialog
        updateEvent.show();
        // Find the input fields and buttons in the dialog layout
        EditText titleEditText = updateEvent.findViewById(R.id.add_title);
        EditText timeChange = updateEvent.findViewById(R.id.add_time);
        EditText colorChange = updateEvent.findViewById(R.id.add_color);
        EditText dateChange = updateEvent.findViewById(R.id.add_date);
        TextInputEditText noteEditText = updateEvent.findViewById(R.id.note_event);
        ImageView coloricon = updateEvent.findViewById(R.id.add_color_icon);

        ExtendedFloatingActionButton updateButton = updateEvent.findViewById(R.id.save_event);
        ImageButton cancelButton = updateEvent.findViewById(R.id.close_event);

        // Set the event details to the input fields in the dialog
        titleEditText.setText(event.getTitle());
        noteEditText.setText(event.getNote());
        colorChange.setText(event.getColor());
        timeChange.setText(event.getHour() + ":" + event.getMinute());
        dateChange.setText(getMonthtoString(event.getMonth()) + " " + event.getDay() + ", " + event.getYear());

        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        dateChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(fragmentManager, "Material_Date_Picker");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTimeInMillis((Long) selection);
                        dateChange.setText(datePicker.getHeaderText());
                        datehelper = new DateHelperClass(calendar1.get(Calendar.DAY_OF_MONTH),calendar1.get(Calendar.MONTH) + 1,calendar1.get(Calendar.YEAR));




                    }
                });
            }
        });
        MaterialTimePicker timePicker =
                new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(LocalTime.now(ZoneId.of("Asia/Manila")).getHour())
                        .setMinute(LocalTime.now(ZoneId.of("Asia/Manila")).getMinute())
                        .setTitleText("Select Time")
                        .build();
        timeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(fragmentManager, "Material_Time_Picker");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeHelperClass = new TimeHelperClass(timePicker.getMinute(), timePicker.getHour());

                        if(timeHelperClass.getMinute() == 0) {
                            timeChange.setText(timeHelperClass.getHour()+ ":00");
                        } else {
                            String hr_temp = String.format("%d", timeHelperClass.getHour());
                            String min_temp = String.format("%d", timeHelperClass.getMinute());
                            if(timeHelperClass.getHour() < 10) {
                                hr_temp = String.format("0%d", timeHelperClass.getMinute());
                            }
                            if(timeHelperClass.getMinute() < 10) {
                                min_temp = String.format("0%d", timeHelperClass.getMinute());
                            }
                            timeChange.setText(hr_temp+ ":" + min_temp);
                        }
                        //**********Tester************
                        Log.d("Time", "Time: " + timeHelperClass.toString());
                        //*****************************

                    }
                });
            }
        });


        colorChange.setOnClickListener(new View.OnClickListener() {
            int iconColor = R.color.captime_color;
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder.with(context)
                        .setTitle("Choose Color")
                        .initialColor(iconColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                Toast.makeText(context, "Selected color: #" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("OK", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                                iconColor = lastSelectedColor;
                                coloricon.setColorFilter(iconColor);
                                colorChange.setText("#" + Integer.toHexString(lastSelectedColor));
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

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.setColor(pickedColor);
                event.setTitle(titleEditText.getText().toString());
                event.setNote(noteEditText.getText().toString());
                event.setDay(datehelper.getDay());
                event.setMonth(datehelper.getMonth());
                event.setYear(datehelper.getYear());
                event.setHour(timeHelperClass.getHour());
                event.setMinute(timeHelperClass.getMinute());

                updateItem(event, prev, pos);
                wipe();

            }

            private void wipe() {
                titleEditText.getText().clear();
                noteEditText.getText().clear();
                coloricon.setColorFilter(R.color.captime_color);
                colorChange.getText().clear();
                dateChange.getText().clear();
                timeChange.getText().clear();
                updateEvent.dismiss();
            }

            private void updateItem(EventHelperClass e, EventHelperClass prev, int pos) {
                EventHelperClass p = eventArrayList.get(pos);
                db.collection("events").whereEqualTo("email", p.getEmail())
                        .whereEqualTo("title", p.getTitle())
                        .whereEqualTo("note", p.getNote())
                        .whereEqualTo("color", p.getColor())
                        .whereEqualTo("day", p.getDay())
                        .whereEqualTo("month", p.getMonth())
                        .whereEqualTo("year", p.getYear())
                        .whereEqualTo("hour", p.getHour())
                        .whereEqualTo("minute", p.getMinute())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful() && !task.getResult().isEmpty()) {
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    String documentId = documentSnapshot.getId();
                                    db.collection("events")
                                            .document(documentId)
                                            .update("title", e.getTitle(),
                                                    "note", e.getNote(),
                                                    "color", e.getColor(), "day", e.getDay(),
                                                    "month", e.getMonth(), "month", e.getMonth(),
                                                    "year", e.getYear(), "hour", e.getHour(),
                                                    "minute", e.getMinute())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context.getApplicationContext(),  "Updated", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                } else {
                                    Toast.makeText(context.getApplicationContext(),"Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleEditText.getText().clear();
                noteEditText.getText().clear();
                coloricon.setColorFilter(R.color.captime_color);
                colorChange.getText().clear();
                dateChange.getText().clear();
                timeChange.getText().clear();
                updateEvent.dismiss();
            }
        });







    }

    private void deleteItem(EventHelperClass e) {


        db.collection("events").whereEqualTo("email", e.getEmail())
                .whereEqualTo("title", e.getTitle())
                .whereEqualTo("note", e.getNote())
                .whereEqualTo("color", e.getColor())
                .whereEqualTo("day", e.getDay())
                .whereEqualTo("month", e.getMonth())
                .whereEqualTo("year", e.getYear())
                .whereEqualTo("hour", e.getHour())
                .whereEqualTo("minute", e.getMinute())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            db.collection("events")
                                    .document(documentId)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context.getApplicationContext(), "Succesfuly Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context.getApplicationContext(),"Error Occured", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(context.getApplicationContext(),"Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }
    public void setEventArrayList(ArrayList<EventHelperClass> eventArrayList) {
        this.eventArrayList = eventArrayList;
        notifyDataSetChanged();
    }
    public class EventViewHolder extends RecyclerView.ViewHolder {

        TextView title, note;
        MaterialCardView cardView;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.event_CardView);
            title = itemView.findViewById(R.id.event_title);
            note = itemView.findViewById(R.id.event_note);
        }
    }

    public void clear() {
        eventArrayList.clear();
        notifyDataSetChanged();

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