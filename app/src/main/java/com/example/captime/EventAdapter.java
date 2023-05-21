package com.example.captime;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.captime.helperclass.EventHelperClass;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    Context context;
    ArrayList<EventHelperClass> eventArrayList;

    public EventAdapter(Context context, ArrayList<EventHelperClass> eventArrayList) {
        this.context = context;
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
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {
        EventHelperClass event =  eventArrayList.get(position);
        holder.title.setText(event.getTitle());
        holder.note.setText(event.getNote());
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView title, note;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.event_title);
            note = itemView.findViewById(R.id.event_note);
        }
    }
}