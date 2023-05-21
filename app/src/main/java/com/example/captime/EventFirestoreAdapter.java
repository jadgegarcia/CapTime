package com.example.captime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.captime.helperclass.EventHelperClass;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class EventFirestoreAdapter extends FirestoreRecyclerAdapter<EventHelperClass, EventFirestoreAdapter.MyEventHolder> {

    public EventFirestoreAdapter(@NonNull FirestoreRecyclerOptions<EventHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyEventHolder holder, int position, @NonNull EventHelperClass model) {
        // Bind data to your ViewHolder
        holder.bind(model);
    }

    @NonNull
    @Override
    public MyEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create and return an instance of your ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new MyEventHolder(view);
    }

    public class MyEventHolder extends RecyclerView.ViewHolder {

        private TextView title, note;

        public MyEventHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your views here
            title= itemView.findViewById(R.id.event_title);
            note = itemView.findViewById(R.id.event_note);
        }

        public void bind(EventHelperClass model) {
            // Bind data to your views here
            title.setText(model.getTitle());
        }
    }
}