package com.example.captime;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.captime.helperclass.EventHelperClass;
import com.google.android.material.card.MaterialCardView;

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

        int color = Color.parseColor(event.getColor());
        holder.cardView.setCardBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView title, note;
        MaterialCardView cardView;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.event_CardView);
            title = itemView.findViewById(R.id.event_title);
            note = itemView.findViewById(R.id.event_note);
        }
    }
}