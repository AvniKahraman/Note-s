package com.example.notepad2;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad2.databinding.ActivityRowBinding;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private ArrayList<Note_class> noteArrayList;
    private Context context;

    public NoteAdapter(Context context, ArrayList<Note_class> noteArrayList) {
        this.context = context;
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(v.getContext(), Notepad.class);
                    intent.putExtra("info", "old");
                    intent.putExtra("noteId", noteArrayList.get(clickedPosition).id);
                    v.getContext().startActivity(intent);
                }
            }
        });

        holder.binding.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(note.id, holder.getAdapterPosition());
            }
        });
    }

    private void showDeleteConfirmationDialog(final int noteId, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNoteFromDatabase(noteId, position);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteNoteFromDatabase(int noteId, int position) {
        try {
            SQLiteDatabase database = this.openOrCreateDatabase("Note", MODE_PRIVATE, null);
            database.execSQL("DELETE FROM note WHERE id = ?", new Object[]{noteId});
            noteArrayList.remove(position); // Silinen notu listeden kaldır
            noteAdapter.notifyItemRemoved(position); // Adapter'a değişikliği bildir
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDeleteConfirmationDialog(final int noteId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNote(noteId);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteNote(int noteId) {
        // Veritabanından notu silme işlemi burada yapılabilir
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
