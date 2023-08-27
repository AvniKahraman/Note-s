package com.example.notepad2;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad2.Note_class;
import com.example.notepad2.databinding.ActivityRowBinding;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private ArrayList<Note_class> noteArrayList;

    public NoteAdapter(ArrayList<Note_class> noteArrayList) {
        this.noteArrayList = noteArrayList;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityRowBinding binding = ActivityRowBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new NoteHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note_class note = noteArrayList.get(position);
        holder.binding.editView.setImageResource(R.drawable.baseline_edit_24);
        final int clickedPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(v.getContext(), Notepad.class);
                    intent.putExtra("info", "old");
                    intent.putExtra("noteId", noteArrayList.get(clickedPosition).id);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }





    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        ActivityRowBinding binding;

        public NoteHolder(ActivityRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
